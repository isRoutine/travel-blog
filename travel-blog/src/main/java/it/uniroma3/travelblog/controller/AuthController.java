package it.uniroma3.travelblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.travelblog.model.Credentials;
import it.uniroma3.travelblog.model.User;
import it.uniroma3.travelblog.service.CredentialsService;
import it.uniroma3.travelblog.validator.CredentialsValidator;
import it.uniroma3.travelblog.validator.UserValidator;

@Controller
public class AuthController {
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private CredentialsValidator credentialsValidator;
	
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
		return "index";
	}
	
    @GetMapping("/default")
    public String defaultAfterLogin(Model model) {
        
    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialsService.findByUsername(userDetails.getUsername());
    	if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
            return "admin/home";
        }
        return "index";
    }
    
    @PostMapping("/registration/validate")
    public String registerUser(@ModelAttribute("user") User user, BindingResult userBindingResult, @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult, Model model) {

        this.userValidator.validate(user, userBindingResult);
        this.credentialsValidator.validate(credentials, credentialsBindingResult);

        if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
            credentials.setUser(user);
            credentialsService.save(credentials);
            return "registrationSuccessful";
        }
        return "signUp";
    }
    
    /**TODO logica admin-user**/
}
