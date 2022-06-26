package it.uniroma3.travelblog.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.travelblog.model.Experience;
import it.uniroma3.travelblog.service.ExperienceService;

@Component
public class ExperienceValidator implements Validator {
	
	final Integer DESC_MAX_LENGTH = 200;
	final Integer MAX_LENGTH = 50;
    final Integer MIN_LENGTH = 2;
	
	@Autowired
	private ExperienceService expService;
	
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Experience.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Experience experience = (Experience) target;
        String name = experience.getName().trim();
        String description = experience.getDescription().trim();
		
        if (name.length() < MIN_LENGTH || name.length() > MAX_LENGTH)
            errors.rejectValue("name", "size");
        
        if (description.length() < MIN_LENGTH || description.length() > DESC_MAX_LENGTH)
            errors.rejectValue("description", "desc.size");
        
        if(experience.getId() == null || !experience.getName().equals(this.expService.findById(experience.getId()).getName())) {
        	if(this.expService.existByName(experience)){
        		errors.rejectValue("name", "experience.duplication");
        	}
        }
	}

	
	
}
