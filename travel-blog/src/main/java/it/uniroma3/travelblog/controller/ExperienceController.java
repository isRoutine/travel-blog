package it.uniroma3.travelblog.controller;

import java.time.LocalDateTime;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.travelblog.controller.validator.ExperienceValidator;
import it.uniroma3.travelblog.model.Credentials;
import it.uniroma3.travelblog.model.Experience;
import it.uniroma3.travelblog.model.Location;
import it.uniroma3.travelblog.model.User;
import it.uniroma3.travelblog.presentation.FileStorer;
import it.uniroma3.travelblog.service.BookmarkService;
import it.uniroma3.travelblog.service.CredentialsService;
import it.uniroma3.travelblog.service.ExperienceService;
import it.uniroma3.travelblog.service.UserService;

@Controller
@RequestMapping("/experience")
public class ExperienceController {
	
	@Autowired
	private ExperienceService expService;
	
	@Autowired
	private ExperienceValidator experienceValidator;
	
	@Autowired
	private UserService userService;	
	
	@Autowired
	private CredentialsService credentialsService;	
	
	@Autowired
	private BookmarkService bookmarkService;
	
		
	@GetMapping("/{id}")
	public String getExperience(@PathVariable("id") Long id, Model model) {
		model.addAttribute("expereince", this.expService.findById(id));
		return "expereince.html";
	}
	
	@GetMapping("/all")
	public String getExperiences(Model model) {
		model.addAttribute("experience", this.expService.findAll());
		return "/experience/all";
	}
	
	
	@GetMapping("/add")
	public String experienceForm(Model model) {

		model.addAttribute("experience", new Experience());
		model.addAttribute("location", new Location());
		return "/experience/add";
	}	
	
	@PostMapping("/add")
	public String addExperience(@ModelAttribute("experience") Experience exp, BindingResult bindingResult, @ModelAttribute("location") Location location, @RequestParam("file") MultipartFile[] files ,Model model) {
		
		this.experienceValidator.validate(exp, bindingResult);
		
		if(!bindingResult.hasErrors()) {
			exp.setCreationTime(LocalDateTime.now());
			exp.setLocation(location);
			
			// sfrutto le informazioni di spring security per ottenere l'utente attualmente loggato, senza dover 
			// passare per parametri tramite url 	

	    	User user;
	    	try { // loggato normalmente
				UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    	Credentials credentials = credentialsService.findByUsername(userDetails.getUsername());	
	        	user = credentials.getUser();
	        	
	    	} catch(Exception e){ // loggato con oauth
	        	OAuth2User userDetails = (OAuth2User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        	String email = userDetails.getAttribute("email");
	        	user = userService.findByEmail(email);
	    	} 


			// salvo solo l'utente perchè ho la cascade su experience
	    	exp.setUser(user);
			user.addExperience(exp);
			this.userService.save(user);
			Experience expSaved = this.expService.save(exp);
			
			
			// questa parte non credo funzioni ancora...
			// non viene creata la directory
			if(!files[0].isEmpty()){
				int i=0;
				for(MultipartFile file : files) {
					exp.getImgs()[i] = FileStorer.store(file, expSaved.getDirectoryName());
					i++;
				}
				this.expService.save(expSaved);
			}
		
			return "redirect:/profile";
		}
		
		return "experience/add";
	}	
		
	

	@GetMapping("/modify/{id}")
	public String experienceModify(@PathVariable("id") Long id, Model model) {
		Experience oldExperience =  this.expService.findById(id);
		model.addAttribute("experience", oldExperience);
		return "experience/modify";
	}
	
	@PostMapping("/modify")
	public String experienceUpdate(@Valid @ModelAttribute("experience")Experience exp, BindingResult bindingResult, Model model) {
		if(!bindingResult.hasErrors()) {
			this.expService.update(exp);
			return "redirect:/profile";
		}
		else return "/experience/modify";
	}
	
	
	
	@GetMapping("/delete/{id}")
	public String deleteExperience(@PathVariable("id") Long id, Model model) {
		Experience exp = this.expService.findById(id);
		FileStorer.removeImgsAndDir(exp.getDirectoryName(), exp.getImgs());
		
		this.bookmarkService.deleteAllByTarget(exp);
		
		this.expService.deleteById(id);
		return "redirect:/profile";
	}
	
	
	
	
}
