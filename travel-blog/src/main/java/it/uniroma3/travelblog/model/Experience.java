package it.uniroma3.travelblog.model;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class Experience{

	private final static int sLength = 1024;
	private static final int MAX_IMGS = 5;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String name;
	
	@Column(length = sLength)
	private String description;
	
	//@NotBlank
	//@NotNull
	private LocalDateTime creationTime;
	
	private String[] imgs;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Location location;
	
	@ManyToOne
	private User user;
	
	private Integer likes;
	
	
	public Experience() {
		this.imgs = new String[MAX_IMGS];
		this.likes = 0;
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

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}
	
	public void addLike() {
		this.likes += 1;
	}
	
	public void removeLike() {
		this.likes -= 1;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public String[] getImgs() {
		return imgs;
	}

	public void setImgs(String[] imgs) {
		this.imgs = imgs;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public void emptyImgs() {
		this.imgs = new String[MAX_IMGS];
	}

	public void removeImg(String img) {
		for(int i = 0; i < this.imgs.length; i++) {
			if(this.imgs[i] != null && this.imgs[i].equals(img)) this.imgs[i]=null;
		}
	}
	
	public String getDirectoryName() {
		return this.user.getDirectoryName() + "/exp" + this.getId();
	}
	

	@Override
	public int hashCode() {
		return Objects.hash(creationTime, name, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Experience other = (Experience) obj;
		return Objects.equals(creationTime, other.creationTime) && Objects.equals(name, other.name)
				&& Objects.equals(user, other.user);
	}
	
}
