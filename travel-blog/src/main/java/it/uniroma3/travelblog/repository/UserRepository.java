package it.uniroma3.travelblog.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.travelblog.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

}
