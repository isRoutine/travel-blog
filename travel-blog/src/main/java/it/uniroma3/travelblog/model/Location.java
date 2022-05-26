package it.uniroma3.travelblog.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.UniqueElements;

public class Location {

	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@UniqueElements
	private String nation;
	
	@UniqueElements
	private String state;
	
	@UniqueElements
	private String country;
	
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
