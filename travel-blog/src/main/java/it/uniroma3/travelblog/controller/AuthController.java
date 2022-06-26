package it.uniroma3.travelblog.controller;

import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.travelblog.controller.validator.CredentialsValidator;
import it.uniroma3.travelblog.controller.validator.UserValidator;
import it.uniroma3.travelblog.model.Credentials;
import it.uniroma3.travelblog.model.User;
import it.uniroma3.travelblog.presentation.FileStorer;
import it.uniroma3.travelblog.service.BookmarkService;
import it.uniroma3.travelblog.service.CredentialsService;
import it.uniroma3.travelblog.service.LikeService;
import it.uniroma3.travelblog.service.UserService;

@Controller
public class AuthController {
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private CredentialsValidator credentialsValidator;

	@Autowired 
	private UserService userService;
	
	@Autowired
	private BookmarkService bookmarkService;
	
	@Autowired
	private LikeService likeService;

	
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "signUp";
	}
	
	@GetMapping("/login")
	public String showLoginForm(Model model) {
		return "login";
	}
	
	@GetMapping("/logout")
	public String logout(Model model) {
		return "redirect:/";
	}
	
    @GetMapping("/default")
    public String defaultAfterLogin(Model model) {
    	return "redirect:/";
    }
    
    @GetMapping("/default/oauth")
    public String defaultAfterLoginOauth(Model model) {
        
    	OAuth2User userDetails = (OAuth2User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	System.out.println(userDetails.getAttributes().toString());
    	String email = userDetails.getAttribute("email");
    	User user = userService.findByEmail(email);
    	if(user == null) {
    		user = new User();
    		user.setName((String)userDetails.getAttribute("given_name"));
    		user.setSurname((String)userDetails.getAttribute("family_name"));
    		user.setEmail(email);
    		user.setBirthDate(LocalDate.now());
    		userService.save(user);
    	}
    	return "redirect:/";
    }
    
     
    
    @PostMapping("/registration/validate")
    public String registerUser(@ModelAttribute("user") User user, BindingResult userBindingResult, @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult, @RequestParam("file")MultipartFile file, Model model) {

        this.userValidator.validate(user, userBindingResult);
        this.credentialsValidator.validate(credentials, credentialsBindingResult);
        
        if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
        	credentials.setUser(user);
            credentialsService.save(credentials);
            
            if(!file.isEmpty()) {
            	user.setImg(FileStorer.store(file, user.getDirectoryName()));
            	userService.save(user);
            }
            
            return "redirect:/login";
        }
        return "signUp";
    }
    

    @GetMapping("/user/delete/{id}")
	public String deleteUser(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
		User user = this.userService.findById(id);
		Credentials credentials = this.credentialsService.findByUser(user);
		
		this.bookmarkService.deleteAllByOwner(user);
		this.likeService.deleteAllByOwner(user);
		
		try { // evito il caso in cui non sia stata creata la directory per questo user
			// nel caso in cui non avesse ancora inserito esperienze
			FileStorer.dirEmptyEndDelete(user.getDirectoryName());
		} catch(Exception e) {
			
		}
		
		
		try {
			request.logout();
		} catch (ServletException e) {
			e.printStackTrace();
		}
		credentialsService.deleteById(credentials.getId());
		return "redirect:/";
	}
    
	@GetMapping("/user/delete/image/{id}")
	public String deleteImage(@PathVariable("id") Long id, Model model) {
		User user = this.userService.findById(id);
		FileStorer.removeImg(user.getDirectoryName(), user.getImg());
		user.setImg(null);			
		this.userService.save(user);
		
		return this.modifyUser(model);
	}
    
    
//    @GetMapping("/admin/promote")
//    public String promoteUser() {
//    	return "/admin/promote";
//    }
//    
//    @PostMapping("/admin/promote/finalize")
//    public String promotion(@ModelAttribute("username") String username, BindingResult userBindingResult, Model model) {
//    	credentialsService.promote(username);
//    	return "admin/home";
//    }
    
    @GetMapping("/profile")
    public String getProfile(Model model) {
      
    	try {
	    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    	String username = ((UserDetails)principal).getUsername();
	    	Credentials credentials = this.credentialsService.findByUsername(username);
			model.addAttribute("user", credentials.getUser());
			model.addAttribute("me", credentials.getUser().getId());  
        	model.addAttribute("experiences", credentials.getUser().getExperiences());
    	} catch(Exception e) {
        	OAuth2User userDetails = (OAuth2User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        	String email = userDetails.getAttribute("email");
        	model.addAttribute("user",this.userService.findByEmail(email)); 
        	model.addAttribute("me", this.userService.findByEmail(email).getId());   
        	model.addAttribute("experiences", this.userService.findByEmail(email).getExperiences());
    	}
    	return "/user/profile";
    }
	
    @GetMapping("/profile/{id}")
    public String getProfile(@PathVariable("id") Long id, Model model) {
		model.addAttribute("user", userService.findById(id));
		
		try {
	    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    	String username = ((UserDetails)principal).getUsername();
	    	Credentials credentials = this.credentialsService.findByUsername(username);
			model.addAttribute("me", credentials.getUser().getId());
			model.addAttribute("experiences", credentials.getUser().getExperiences());
    	} catch(Exception e) {
        	OAuth2User userDetails = (OAuth2User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        	String email = userDetails.getAttribute("email");
        	model.addAttribute("me", this.userService.findByEmail(email).getId());
        	model.addAttribute("experiences", this.userService.findByEmail(email).getExperiences());
    	}
		
		
    	return "/user/profile";
    }
    
    @GetMapping("/user/modify")
    public String modifyUser(Model model) {
    	
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	String username = ((UserDetails)principal).getUsername();
    	Credentials credentials = this.credentialsService.findByUsername(username);
    	
		model.addAttribute("user", credentials.getUser());
    	return "/user/modify";
    }
    
    @PostMapping("/user/modify")
	public String updateProfile(@ModelAttribute("user") User user, BindingResult bindingResult,@RequestParam("file")MultipartFile file, Model model) {
    	this.userValidator.validate(user, bindingResult);

		
    	if(!bindingResult.hasErrors()) {
			
			if(!file.isEmpty()) {
				FileStorer.removeImg(user.getDirectoryName(),user.getImg());
				user.setImg(FileStorer.store(file, user.getDirectoryName()));
			}
			
			this.userService.save(user);
			
			return "redirect:/profile";
		}
		else return "redirect:/user/modify";
	}
}
