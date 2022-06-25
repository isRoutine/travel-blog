package it.uniroma3.travelblog.controller;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.travelblog.model.Credentials;
import it.uniroma3.travelblog.model.Experience;
import it.uniroma3.travelblog.model.Location;
import it.uniroma3.travelblog.model.User;
import it.uniroma3.travelblog.presentation.FileStorer;
import it.uniroma3.travelblog.service.CredentialsService;
import it.uniroma3.travelblog.service.ExperienceService;
import it.uniroma3.travelblog.service.UserService;

@Controller
@RequestMapping("/experience")
public class ExperienceController {
	
	private static final int EXP_FOR_PAGE = 5;

	@Autowired
	private ExperienceService expService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CredentialsService credentialsService;	
	
	
	private List<Experience> getSortedExperiences() {
		List<Experience> experieces = this.expService.findAll();
		experieces.sort(new Comparator<Experience>() {
			@Override
			public int compare(Experience o1, Experience o2) {
				return o1.getCreationTime().compareTo(o2.getCreationTime());
			}
		});
		return experieces;
	}
		
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
	public String addExperience(@ModelAttribute("experience") Experience exp, @ModelAttribute("location") Location location,
											@RequestParam("file") MultipartFile[] files ,Model model) {
		
		exp.setCreationTime(LocalDateTime.now());
		exp.setLocation(location);
		
		// sfrutto le informazioni di spring security per ottenere l'utente attualmente loggato, senza dover 
		// passare per parametri tramite url
    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialsService.findByUsername(userDetails.getUsername());	
    	User user = credentials.getUser();   	
		exp.setUser(user);



		// salvo solo l'utente perchè ho la cascade su experience
		user.addExperience(exp);
		User userSaved = this.userService.save(exp.getUser());
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
	
	
	
//	@PostMapping("/add")
//	public String addExperience(@Valid @ModelAttribute("experience") Experience exp, @RequestParam("file") MultipartFile[] files, BindingResult bindingResult, Model model) {
//		if(!bindingResult.hasErrors()) {
//			int i=0;
// 			for(MultipartFile file : files) {
// 				exp.getImgs()[i] = FileStorer.store(file,  exp.getDirectoryName());
// 				i++;
//			}
// 			exp.getUser().addExperience(exp);
// 			this.userService.save(exp.getUser());
//			this.expService.save(exp);
//			model.addAttribute("experience", this.expService.findById(exp.getId()));
//			return "/experience/info";
//		}
//		else return "/experience/form";
//	}	
	

	@GetMapping("/modify/{id}")
	public String experienceModify(@PathVariable("id") Long id, Model model) {
		Experience oldExperience =  this.expService.findById(id);
		model.addAttribute("experience", oldExperience);
		return "experience/modify";
	}
	
	@PostMapping("/modify")
	public String experienceUpdate(@Valid @ModelAttribute("experience")Experience exp, Model model) {
//		if(!bindingResult.hasErrors()) {
//			FileStorer.dirRename(this.expService.findById(exp.getId()).getDirectoryName() , exp.getDirectoryName());
//			if (files != null) {
//				FileStorer.dirEmpty(exp.getDirectoryName());
//				exp.emptyImgs();
//				int i=0;
//				for(MultipartFile file : files) {
//					if(!file.isEmpty()) {
//						exp.getImgs()[i]= FileStorer.store(file, exp.getDirectoryName());
//						i++;
//					}
//				}
//			}
			
			this.expService.update(exp);
			return "redirect:/profile";
//		}
//		else return "/experience/modify";
	}
	
	
	
	@GetMapping("/delete/{id}")
	public String deleteExperience(@PathVariable("id") Long id, Model model) {
		Experience exp = this.expService.findById(id);
		FileStorer.removeImgsAndDir(exp.getDirectoryName(), exp.getImgs());
		this.expService.deleteById(id);
		return "redirect:/profile";
	}
	
	@GetMapping("/delete/{id}/{img}")
	public String deleteImage(@PathVariable("id") Long id, @PathVariable("img") String img, Model model) {
		Experience exp = this.expService.findById(id);
		for(String currImg : exp.getImgs()) {
			if(currImg != null && currImg.equals(img)) {
				exp.removeImg(img);
				FileStorer.removeImg(exp.getDirectoryName(), img);
			}
			
		}
			
		this.expService.save(exp);
		model.addAttribute("expereince", this.expService.findById(id));
		return "experience/modify";
	}
	
	
	/**Primo metodo da invocare per ottenere la home page**/
	@GetMapping("/home/get")
	public String getExperiencesHome(Model model) {
		List<Experience> experieces = this.expService.findAll();
		experieces.sort(new Comparator<Experience>() {
			@Override
			public int compare(Experience o1, Experience o2) {
				return o1.getCreationTime().compareTo(o2.getCreationTime());
			}
		});
		
		model.addAttribute("currPage", 0);
		model.addAttribute("experiences", experieces.subList(0, EXP_FOR_PAGE));
		return "/experience/all";
	}
	
	/**Metodo da invocare per ottenere il caricamento delle esperienze della pagina successiva**/
	@GetMapping("/home/next/{page}")
	public String getNextExperiences(@PathVariable("page") Integer page, Model model) {
		List<Experience> experieces = getSortedExperiences();
		Integer currPage = page+1;
		model.addAttribute("currPage", currPage);
		model.addAttribute("experiences", experieces.subList(currPage*EXP_FOR_PAGE, (currPage*EXP_FOR_PAGE)+EXP_FOR_PAGE));
		return "/experience/all"; // ritornriamo sempre l'index ...
	}
	
	/**Metodo da invocare per ottenere il caricamento delle esperienze della pagina precedente**/
	@GetMapping("/home/prev/{page}")
	public String getPrevExperiences(@PathVariable("page") Integer page, Model model) {
		List<Experience> experieces = getSortedExperiences();
		Integer currPage = page-1;
		model.addAttribute("currPage", currPage);
		model.addAttribute("experiences", experieces.subList(currPage*EXP_FOR_PAGE, (currPage*EXP_FOR_PAGE)+EXP_FOR_PAGE));
		return "/experience/all";
	}

	
}
