package it.uniroma3.travelblog.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.travelblog.model.Bookmark;
import it.uniroma3.travelblog.model.User;

public interface BookmarkRepository extends CrudRepository<Bookmark, Long> {
	public List<Bookmark> findAllByOwner(User owner);
}
