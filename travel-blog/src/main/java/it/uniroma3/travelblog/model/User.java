package it.uniroma3.travelblog.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.UniqueElements;

@Entity
public class User {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotBlank
	@NotNull
	private String name;
	
	@NotBlank
	@NotNull
	private String surname;
	
	@NotNull
	private LocalDate birthDate;
	
	@NotBlank
	@NotNull
	private String userName;
	
	@NotNull
	@NotBlank
	@UniqueElements
	private String email;
	
	@OneToMany
	private List<Experience> experiences;
	
	
	/*todo pwd*/
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
