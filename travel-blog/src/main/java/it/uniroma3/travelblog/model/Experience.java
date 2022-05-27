package it.uniroma3.travelblog.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Experience {

	private final static int sLength = 1024;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = sLength)
	private String description;
	
	@NotBlank
	@NotNull
	private LocalDateTime creationTime;
	
	@OneToMany
	private List<Image> imgs;
	
	@ManyToOne
	private Location location;
	
	
	
	public Experience() {
		this.imgs = new ArrayList<Image>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public List<Image> getImg() {
		return imgs;
	}

	public void setImg(List<Image> imgs) {
		this.imgs = imgs;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	
	
}
