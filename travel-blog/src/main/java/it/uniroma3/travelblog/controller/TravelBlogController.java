package it.uniroma3.travelblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.travelblog.model.Credentials;
import it.uniroma3.travelblog.service.CredentialsService;

@Controller
public class TravelBlogController {
	
	@Autowired CredentialsService credentialsService;
	
	@GetMapping("/profile")
	public String getProfile(Model model) {
    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialsService.findByUsername(userDetails.getUsername());
		model.addAttribute("user", credentials.getUser());
		return "profile";
	}
}
