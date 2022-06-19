package it.uniroma3.travelblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TravelBlogController {
	
	
	@GetMapping("/profile")
	public String getProfile() {
		return "profile";
	}
}
