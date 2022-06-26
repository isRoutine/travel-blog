package it.uniroma3.travelblog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import it.uniroma3.travelblog.model.Credentials;
import it.uniroma3.travelblog.model.Experience;
import it.uniroma3.travelblog.model.Like;
import it.uniroma3.travelblog.model.User;
import it.uniroma3.travelblog.service.CredentialsService;
import it.uniroma3.travelblog.service.ExperienceService;
import it.uniroma3.travelblog.service.LikeService;
import it.uniroma3.travelblog.service.UserService;

@Controller
@RequestMapping("/like")
public class LikeController {

	@Autowired
	private LikeService likeService;
	
	@Autowired
	private ExperienceService expService;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private UserService userService;
	
	
	@GetMapping("/add/{id}")
	String addLike(@PathVariable("id") Long id, Model model) {
		Like like = new Like();
		Experience exp = this.expService.findById(id);
		
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
    	
    	like.setOwner(user);
    	like.setTarget(exp);
    	exp.addLike();
    	
    	this.expService.save(exp);
    	this.likeService.save(like);
		return "redirect:/";
	}
	
	@GetMapping("/all")
	String getLikes(Model model) {
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
    	
		List<Like> likes = this.likeService.findAllByUser(user);
		
		model.addAttribute("likes", likes);
		model.addAttribute("user", user);
		model.addAttribute("me", user.getId());
		return "/user/likes";
	}
	
	@GetMapping("/delete/{id}") /*id del like*/
	String deleteLike(@PathVariable("id") Long id, Model model) {
		Like like = this.likeService.findById(id);
		like.getTarget().removeLike();
		this.expService.save(like.getTarget());
		this.likeService.deleteById(id);
		
		return "redirect:/";
	}
	
	@GetMapping("/delete/byExp/{id}") /*id dell'esperienza*/
	String deleteLikeByExperienceAndUser(@PathVariable("id") Long id, Model model) {
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
    	Experience exp = this.expService.findById(id);
    	exp.removeLike();
    	this.expService.save(exp);
		this.likeService.deleteByTargetAndOwner(exp, user);
		
		return "redirect:/";
	}
	
}
