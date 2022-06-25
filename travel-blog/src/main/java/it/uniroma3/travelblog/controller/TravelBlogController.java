package it.uniroma3.travelblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.travelblog.service.ExperienceService;

@Controller
public class TravelBlogController {
	
	@Autowired ExperienceService expService;
	
//	@GetMapping("/")
//	public String getHomePage(Model model) {
//		model.addAttribute("experiences", this.expService.findAll());
//		return "index";
//	}
}
