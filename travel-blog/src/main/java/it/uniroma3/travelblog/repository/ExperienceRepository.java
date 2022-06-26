package it.uniroma3.travelblog.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.travelblog.model.Experience;
import it.uniroma3.travelblog.model.User;

public interface ExperienceRepository extends CrudRepository<Experience, Long>{

	public boolean existsByName(String name);
	
}
