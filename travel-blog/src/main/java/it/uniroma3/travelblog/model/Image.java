package it.uniroma3.travelblog.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.web.multipart.MultipartFile;

@Entity
public class Image {

	/*
	 * da sistemrare, ha senso creare una cartella per ogni utente?
	 */
	public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/uploads";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String path;
	
	private String description;

	
	public Image(MultipartFile file) {
		//cero la directory se non esiste
		new File(uploadDirectory).mkdir();
		Path fileNameAndPath  = Paths.get(uploadDirectory, file.getOriginalFilename());
		
		try {
			Files.write(fileNameAndPath, file.getBytes());
			this.path = file.getOriginalFilename().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
