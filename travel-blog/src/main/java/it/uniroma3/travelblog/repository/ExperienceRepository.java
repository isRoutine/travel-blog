package it.uniroma3.travelblog.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.travelblog.model.Experience;

public interface ExperienceRepository extends CrudRepository<Experience, Long>{

	public boolean existsByName(String name);

	public List<Experience> findAllByOrderByCreationTimeDesc();
	
}
