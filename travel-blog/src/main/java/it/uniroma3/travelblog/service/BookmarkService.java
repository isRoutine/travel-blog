package it.uniroma3.travelblog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.travelblog.model.Bookmark;
import it.uniroma3.travelblog.model.Experience;
import it.uniroma3.travelblog.model.User;
import it.uniroma3.travelblog.repository.BookmarkRepository;


@Service
public class BookmarkService {

	@Autowired
	private BookmarkRepository bookmarkRepository;
	
	@Transactional
	public Bookmark findById(Long id) {
		Optional<Bookmark> result = this.bookmarkRepository.findById(id);
		return result.orElse(null);
	}
		
    @Transactional
    public Bookmark save(Bookmark bookmark) {
        return this.bookmarkRepository.save(bookmark);
    }

    @Transactional
	public void deleteById(Long id) {
		this.bookmarkRepository.deleteById(id);
	}
	
	public List<Bookmark> findAllByUser(User user){
		return this.bookmarkRepository.findAllByOwner(user);
	}

	@Transactional
	public void deleteAllByTarget(Experience exp) {
		this.bookmarkRepository.deleteAllByTarget(exp);
		
	}
	
}
