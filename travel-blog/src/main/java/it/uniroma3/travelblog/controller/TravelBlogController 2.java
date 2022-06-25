package it.uniroma3.travelblog.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.travelblog.model.Experience;
import it.uniroma3.travelblog.service.ExperienceService;

@Controller
public class TravelBlogController {
	
	private static final int EXP_FOR_PAGE = 5;
	@Autowired ExperienceService expService;
	
	private List<Experience> getSortedExperiences() {
		List<Experience> experiences = this.expService.findAll();
		experiences.sort(new Comparator<Experience>() {
			@Override
			public int compare(Experience o1, Experience o2) {
				return -o1.getCreationTime().compareTo(o2.getCreationTime());
			}
		});
		return experiences;
	}	
	
	
	/**Primo metodo da invocare per ottenere la home page**/
	@GetMapping("/")
	public String getExperiencesHome(Model model) {
		List<Experience> experiences = this.getSortedExperiences();
		
		int expSize = experiences.size();
		int limit = EXP_FOR_PAGE;
		if(experiences.size() < EXP_FOR_PAGE)
			limit = expSize;
		
		model.addAttribute("currPage", 0);
		model.addAttribute("experiences", experiences.subList(0, limit));
		model.addAttribute("size", expSize);
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
		model.addAttribute("currPage", currPage);
		model.addAttribute("experiences", experiences.subList(currPage*EXP_FOR_PAGE, (currPage*EXP_FOR_PAGE)+EXP_FOR_PAGE));
		model.addAttribute("size", experiences.size());
		return "index";
	}

}
