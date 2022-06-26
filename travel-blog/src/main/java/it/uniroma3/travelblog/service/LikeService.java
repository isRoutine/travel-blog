package it.uniroma3.travelblog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.travelblog.model.Experience;
import it.uniroma3.travelblog.model.Like;
import it.uniroma3.travelblog.model.User;
import it.uniroma3.travelblog.repository.LikeRepository;


@Service
public class LikeService {

	@Autowired
	private LikeRepository likeRepositoy;
	
	@Transactional
	public Like findById(Long id) {
		Optional<Like> result = this.likeRepositoy.findById(id);
		return result.orElse(null);
	}
		
    @Transactional
    public Like save(Like like) {
        return this.likeRepositoy.save(like);
    }

    @Transactional
	public void deleteById(Long id) {
		this.likeRepositoy.deleteById(id);
	}
	
	public List<Like> findAllByUser(User user){
		return this.likeRepositoy.findAllByOwner(user);
	}

	@Transactional
	public void deleteAllByTarget(Experience exp) {
		this.likeRepositoy.deleteAllByTarget(exp);
		
	}

	@Transactional
	public void deleteAllByOwner(User user) {
		this.likeRepositoy.deleteAllByOwner(user);
		
	}
	
	@Transactional
	public void deleteByTargetAndOwner(Experience exp, User user) {
		this.likeRepositoy.deleteByTargetAndOwner(exp, user);
	}
	
}
