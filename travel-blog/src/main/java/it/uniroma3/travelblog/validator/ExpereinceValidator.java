package it.uniroma3.travelblog.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.travelblog.model.Experience;
import it.uniroma3.travelblog.service.ExperienceService;

@Component
public class ExpereinceValidator implements Validator {

	@Autowired
	private ExperienceService expService;
	
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Experience.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(this.expService.existByUserAndDescription((Experience)target)){
			errors.reject("espereinza.duplicato");
		}
	}

	
	
}
