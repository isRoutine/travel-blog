package it.uniroma3.travelblog.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.travelblog.model.User;
import it.uniroma3.travelblog.service.UserService;

@Component
public class UserValidator implements Validator {
	
	final Integer MAX_NAME_LENGTH = 100;
    final Integer MIN_NAME_LENGTH = 2;
	
    @Autowired
    private UserService userService;

    @Override
    public void validate(Object obj, Errors errors) {
        User user = (User) obj;
        String name = user.getName().trim();
        String surname = user.getSurname().trim();
        String email = user.getEmail().trim();

        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH)
            errors.reject("nome.size");

        if (surname.length() < MIN_NAME_LENGTH || surname.length() > MAX_NAME_LENGTH)
            errors.reject("cognome.size");
        
        if (!isEmailValid(email))
            errors.reject("email.invalid");
            
        else if (this.userService.findByEmail(email) != null)
            errors.reject("email.duplicate");
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }
    
    private boolean isEmailValid(String email) {
        String regexPattern = "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@"
                + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$";
        return Pattern.compile(regexPattern).matcher(email).matches();
    }

}
