package it.uniroma3.travelblog.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.travelblog.model.Credentials;
import it.uniroma3.travelblog.model.User;

public interface CredentialsRepository extends CrudRepository<Credentials,Long> {
	
	public Optional<Credentials> findByUsername(String username);

	public Credentials findByUser(User user);	
}
