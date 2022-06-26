package it.uniroma3.travelblog.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Bookmark {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	private User owner;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	private Experience target;

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Experience getTarget() {
		return target;
	}

	public void setTarget(Experience target) {
		this.target = target;
	}

}
