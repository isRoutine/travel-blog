package it.uniroma3.travelblog.controller;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.travelblog.model.Experience;
import it.uniroma3.travelblog.presentation.FileStorer;
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
	
	@PostMapping("/add")
	public String addExperience(@Valid @ModelAttribute("experience") Experience exp, @RequestParam("file") MultipartFile[] files, BindingResult bindingResult, Model model) {
		if(!bindingResult.hasErrors()) {
			int i=0;
 			for(MultipartFile file : files) {
 				exp.getImgs()[i] = FileStorer.store(file,  exp.getDirectoryName());
 				i++;
			}
 			
			this.expService.save(exp);
			model.addAttribute("experience", this.expService.findById(exp.getId()));
			return "/experience/info";
		}
		else return "/experience/form";
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
	
	@GetMapping("/delete/{id}")
	public String deleteExperience(@PathVariable("id") Long id, Model model) {
		Experience exp = this.expService.findById(id);
		FileStorer.removeImgsAndDir(exp.getDirectoryName(), exp.getImgs());
		this.expService.deleteById(id);
		return "index.html";
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
	
	
	/*
	 * suppongo di avere l'id dell'user
	 */
	@GetMapping("/form/{id}")
	public String expereinceForm(@PathVariable("id") Long id, Model model) {
		Experience exp =  new Experience();
		exp.setCreationTime(LocalDateTime.now());
		exp.setUser(userService.findById(id));
	
		model.addAttribute("expereince",exp);
		return "expereince.html";
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
		List<Experience> experieces = this.expService.findAll();
		experieces.sort(new Comparator<Experience>() {
			@Override
			public int compare(Experience o1, Experience o2) {
				return o1.getCreationTime().compareTo(o2.getCreationTime());
			}
		});
		Integer currPage = page+1;
		model.addAttribute("currPage", currPage);
		model.addAttribute("experiences", experieces.subList(currPage*EXP_FOR_PAGE, (currPage*EXP_FOR_PAGE)+EXP_FOR_PAGE));
		return "/experience/all";
	}
	
	/**Metodo da invocare per ottenere il caricamento delle esperienze della pagina precedente**/
	@GetMapping("/home/prev/{page}")
	public String getPrevExperiences(@PathVariable("page") Integer page, Model model) {
		List<Experience> experieces = this.expService.findAll();
		experieces.sort(new Comparator<Experience>() {
			@Override
			public int compare(Experience o1, Experience o2) {
				return o1.getCreationTime().compareTo(o2.getCreationTime());
			}
		});
		Integer currPage = page-1;
		model.addAttribute("currPage", currPage);
		model.addAttribute("experiences", experieces.subList(currPage*EXP_FOR_PAGE, (currPage*EXP_FOR_PAGE)+EXP_FOR_PAGE));
		return "/experience/all";
	}

	
	@GetMapping("/modify/{id}")
	public String experienceModify(@PathVariable("id") Long id, Model model) {
		Experience oldExperience =  this.expService.findById(id);
		model.addAttribute("experience", oldExperience);
		return "experience/modify";
	}
	
	@PostMapping("/modify")
	public String experienceUpdate(@Valid @ModelAttribute("experience")Experience exp, @RequestParam("files")MultipartFile[] files, BindingResult bindingResult, Model model) {
		if(!bindingResult.hasErrors()) {
			if (files != null) {
				FileStorer.dirEmpty(exp.getDirectoryName());
				exp.emptyImgs();
				int i=0;
				for(MultipartFile file : files) {
					if(!file.isEmpty()) {
						exp.getImgs()[i]= FileStorer.store(file, exp.getDirectoryName());
						i++;
					}
				}
			}
			
			this.expService.save(exp);
			model.addAttribute("expereince", this.expService.findById(exp.getId()));
			return "/expereince/info";
		}
		else return "/experience/modify";
	}
	
}
