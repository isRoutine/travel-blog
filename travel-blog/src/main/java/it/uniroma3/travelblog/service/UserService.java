package it.uniroma3.travelblog.service;

import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.travelblog.model.User;
import it.uniroma3.travelblog.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired private UserRepository userRepository;

	@Transactional
	public void save(User user) {
		userRepository.save(user);
	}
	
	@Transactional
	public void delete(User user) {
		userRepository.delete(user);
	}
	
	@Transactional
	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}
	
	// non necessaria la notazione @Transactional, sono metodi di lettura
	public User findById(Long id) {
		return userRepository.findById(id).get();
	}
	
	public List<User> findAll() {
		List<User> users = new LinkedList<User>();
		for(User u : userRepository.findAll()) {
			users.add(u);
		}
		return users;	
	}
}
