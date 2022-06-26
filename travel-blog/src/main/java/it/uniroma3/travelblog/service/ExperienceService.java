package it.uniroma3.travelblog.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.travelblog.model.Experience;
import it.uniroma3.travelblog.repository.ExperienceRepository;

@Service
public class ExperienceService {

	@Autowired
	private ExperienceRepository expRepo;
	
	
	@Transactional
	public Experience save(Experience exp) {
		return this.expRepo.save(exp);
	}
	
	@Transactional
	public void update(Experience experience) {
		// TODO Auto-generated method stub
		Experience foo = this.expRepo.findById(experience.getId()).get();
		foo.setName(experience.getName());
		foo.setDescription(experience.getDescription());
		foo.getLocation().setCountry(experience.getLocation().getCountry());
		foo.getLocation().setCity(experience.getLocation().getCity());
		this.expRepo.save(foo);
	}
	
	public Experience findById(Long id) {
		return this.expRepo.findById(id).get();
	}
	
	public List<Experience> findAll(){
		List<Experience> experiences = new ArrayList<Experience>();
		
		for(Experience exp : this.expRepo.findAll()) {
			experiences.add(exp);
		}
		
		return experiences;
	}
	
	@Transactional
	public void deleteById(Long id) {
		this.expRepo.deleteById(id);
	}

	public boolean existByName(Experience experience) {
		return this.expRepo.existsByName(experience.getName());
	}
	
}
