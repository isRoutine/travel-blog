package it.uniroma3.travelblog.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.travelblog.model.Bookmark;
import it.uniroma3.travelblog.model.Credentials;
import it.uniroma3.travelblog.model.Experience;
import it.uniroma3.travelblog.model.Like;
import it.uniroma3.travelblog.model.User;
import it.uniroma3.travelblog.service.BookmarkService;
import it.uniroma3.travelblog.service.CredentialsService;
import it.uniroma3.travelblog.service.ExperienceService;
import it.uniroma3.travelblog.service.LikeService;
import it.uniroma3.travelblog.service.UserService;

@Controller
public class TravelBlogController {
	
	private static final int EXP_FOR_PAGE = 5;
	@Autowired ExperienceService expService;
	@Autowired private CredentialsService credentialsService;
	@Autowired private UserService userService;
	@Autowired private BookmarkService bookmarkService;
	@Autowired private LikeService likeService;
	
	private List<Experience> getSortedExperiences() {
//		List<Experience> experiences = this.expService.findAll();
//		experiences.sort(new Comparator<Experience>() {
//			@Override
//			public int compare(Experience o1, Experience o2) {
//				return -o1.getCreationTime().compareTo(o2.getCreationTime());
//			}
//		});
		List<Experience> experiences = this.expService.findSortedExperiences();	
		return experiences;
	}	
	
	private void setupUserInfo(Model model) {
		User user;
		try {
			try { // loggato normalmente
				UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    	Credentials credentials = credentialsService.findByUsername(userDetails.getUsername());	
	        	user = credentials.getUser();
	        	
	    	} catch(Exception e){ // loggato con oauth
	        	OAuth2User userDetails = (OAuth2User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        	String email = userDetails.getAttribute("email");
	        	user = userService.findByEmail(email);
	    	}
			
        	List<Experience> booked = new ArrayList<Experience>();
        	for(Bookmark b : this.bookmarkService.findAllByUser(user)) {
        		booked.add(b.getTarget());
        	}
        	
        	List<Experience> liked = new ArrayList<Experience>();
        	for(Like l : this.likeService.findAllByUser(user)) {
        		liked.add(l.getTarget());
        	}
        	
        	model.addAttribute("bookmarks", booked);
        	model.addAttribute("likes", liked);
			
		} catch(Exception e){}
	}
	
	/**Primo metodo da invocare per ottenere la home page**/
	@GetMapping("/")
	public String getExperiencesHome(Model model) {
		List<Experience> experiences = this.getSortedExperiences();
		
		int expSize = experiences.size();
		int limit = EXP_FOR_PAGE;
		if(experiences.size() < EXP_FOR_PAGE)
			limit = expSize;
		
		boolean hasNext = false;
		
		if(expSize > (0*EXP_FOR_PAGE)+EXP_FOR_PAGE)
			hasNext = true;
		
		setupUserInfo(model);
    	
		model.addAttribute("currPage", 0);
		model.addAttribute("experiences", experiences.subList(0, limit));
		model.addAttribute("hasNext", hasNext);
		return "index";
	}


	
	
	/**Metodo da invocare per ottenere il caricamento delle esperienze della pagina successiva**/
	@GetMapping("/next/{page}")
	public String getNextExperiences(@PathVariable("page") Integer page, Model model) {
		List<Experience> experiences = getSortedExperiences();
		Integer currPage = page+1;
		
		int expSize = experiences.size();
		int limit = (currPage*EXP_FOR_PAGE)+EXP_FOR_PAGE;
		
		if(expSize < limit)
			limit = expSize;
		
		boolean hasNext = false;
		
		if(expSize > (currPage*EXP_FOR_PAGE)+EXP_FOR_PAGE)
			hasNext = true;
		
		setupUserInfo(model);
		
		model.addAttribute("experiences", experiences.subList(currPage*EXP_FOR_PAGE, limit));			
		model.addAttribute("currPage", currPage);
		model.addAttribute("hasNext", hasNext);

		return "index"; // ritornriamo sempre l'index ...
	}
	
	/**Metodo da invocare per ottenere il caricamento delle esperienze della pagina precedente**/
	@GetMapping("/prev/{page}")
	public String getPrevExperiences(@PathVariable("page") Integer page, Model model) {
		List<Experience> experiences = getSortedExperiences();
		Integer currPage = page-1;
		
		setupUserInfo(model);
		
		model.addAttribute("currPage", currPage);
		model.addAttribute("experiences", experiences.subList(currPage*EXP_FOR_PAGE, (currPage*EXP_FOR_PAGE)+EXP_FOR_PAGE));
		model.addAttribute("hasNext", true);
		return "index";
	}	
	

}
