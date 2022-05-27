package it.uniroma3.travelbolg.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.travelblog.model.Experience;
import it.uniroma3.travelblog.model.Image;
import it.uniroma3.travelblog.service.ExperienceService;

@Controller
public class ExperienceController {
	
	@Autowired
	private ExperienceService expService;
	
	/*
	 * manca il controllo degli errori con validator
	 * */
	@PostMapping("/exp")
	public String addExperience(@Valid @ModelAttribute("experience") Experience exp, @RequestParam("file") MultipartFile file, BindingResult bindingResult) {
		
		if(!bindingResult.hasErrors()) {
			exp.addImage(new Image(file));
			this.expService.save(exp);
			return "expereince.html";
		}
		
		return "expereinceForm.html";
	}
	
	@GetMapping("/experience/{id}")
	public String getExperience(@PathVariable("id") Long id, Model model) {
		model.addAttribute("expereinxe", this.expService.findById(id));
		return "expereince.html";
	}
	
	@GetMapping("/deleteExperience/{id}")
	public String deleteExperience(@PathVariable("id") Long id, Model model) {
		this.expService.deleteById(id);
		return "index.html";
	}
	
	@GetMapping("/expereinceForm")
	public String expereinceForm(Model model) {
		model.addAttribute("expereince", new Experience());
		return "expereince.html";
	}
	
	
}
