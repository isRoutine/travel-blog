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
	public void save(Experience exp) {
		this.expRepo.save(exp);
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
	
	public boolean existByUserAndDescription(Experience exp) {
		return this.expRepo.existsByUserAndDescription(exp.getUser(), exp.getDescription());
	}
	
}
