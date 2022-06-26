package it.uniroma3.travelblog.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.travelblog.model.Experience;
import it.uniroma3.travelblog.model.Like;
import it.uniroma3.travelblog.model.User;

public interface LikeRepository extends CrudRepository<Like, Long> {
	public List<Like> findAllByOwner(User owner);

	public void deleteAllByTarget(Experience target);

	public void deleteAllByOwner(User owner);

	public void deleteByTargetAndOwner(Experience target, User owner);
}
