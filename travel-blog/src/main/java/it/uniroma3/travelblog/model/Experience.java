package it.uniroma3.travelblog.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
public class Experience {

	private final static int sLength = 1024;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = sLength)
	private String description;
	
	@Min(0)
	@Max(5)
	private Integer rate; //selfRate o userRate??
	
	@ManyToOne
	private Location location;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
}
