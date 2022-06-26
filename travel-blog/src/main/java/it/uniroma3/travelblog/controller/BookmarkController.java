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

import it.uniroma3.travelblog.model.Bookmark;
import it.uniroma3.travelblog.model.Credentials;
import it.uniroma3.travelblog.model.Experience;
import it.uniroma3.travelblog.model.User;
import it.uniroma3.travelblog.service.BookmarkService;
import it.uniroma3.travelblog.service.CredentialsService;
import it.uniroma3.travelblog.service.ExperienceService;
import it.uniroma3.travelblog.service.UserService;

@Controller
@RequestMapping("/bookmark")
public class BookmarkController {

	@Autowired
	private BookmarkService bookmarkService;
	
	@Autowired
	private ExperienceService expService;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private UserService userService;
	
	
	@GetMapping("/add/{id}")
	String addBookmark(@PathVariable("id") Long id, Model model) {
		Bookmark bookmark = new Bookmark();
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
    	
    	bookmark.setOwner(user);
    	bookmark.setTarget(exp);
    	
    	this.bookmarkService.save(bookmark);
		return "redirect:/";
	}
	
	@GetMapping("/all")
	String getBookmark(Model model) {
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
    	
		List<Bookmark> bookmarks = this.bookmarkService.findAllByUser(user);
		
		model.addAttribute("bookmarks", bookmarks);
		model.addAttribute("user", user);
		model.addAttribute("me", user.getId());
		return "/user/bookmarks";
	}
	
	@GetMapping("/delete/{id}") /*id del bookmark*/
	String deleteBookmark(@PathVariable("id") Long id, Model model) {
		this.bookmarkService.deleteById(id);
		
		return "redirect:/bookmark/all";
	}
	
	@GetMapping("/delete/byExp/{id}") /*id dell'esperienza*/
	String deleteBookmarkByExperienceAndUser(@PathVariable("id") Long id, Model model) {
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
    	
		this.bookmarkService.deleteByTargetAndOwner(this.expService.findById(id), user);
		
		return "redirect:/";
	}
	
}
