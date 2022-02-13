package com.basava.rest.webservices.restfulwebservices;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.basava.rest.webservices.restfulwebservices.dao.PostRepository;
import com.basava.rest.webservices.restfulwebservices.dao.UserDaoService;
import com.basava.rest.webservices.restfulwebservices.dao.UserRepository;
import com.basava.rest.webservices.restfulwebservices.exception.UserNotFoundException;
import com.basava.rest.webservices.restfulwebservices.model.Post;
import com.basava.rest.webservices.restfulwebservices.model.User;

@RestController
public class UserJpaController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	//retrieveAllUsers
	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers () {
		return userRepository.findAll();
	}
	
	//retrieveAllUser(ind id)
	@GetMapping("/jpa/users/{id}")
	public EntityModel<Optional<User>> retrieveOneUsers (@PathVariable Integer id) {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) 
			throw new UserNotFoundException("id-"+id);
		EntityModel<Optional<User>> model = EntityModel.of(user);
		WebMvcLinkBuilder linkToUser = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(this.getClass())
				.retrieveAllUsers());
		model.add(linkToUser.withRel("all-user"));
		return model;
	}
	
	
	//retrieveAllUser(ind id)
	@PostMapping("/jpa/users/")
	public ResponseEntity<Object> saveUser (@Valid @RequestBody User user) {
	  User savedUser =userRepository.save(user);
	  URI location = ServletUriComponentsBuilder
			  .fromCurrentRequest()
			  .path("/{id}")
			  .buildAndExpand(savedUser.getId()).toUri();
	  
	  return ResponseEntity.created(location).build();
	}
	
	
	@DeleteMapping("/jpa/users/{id}")
	public void deleteUser (@PathVariable Integer id) {
	  userRepository.deleteById(id);
	}
	
	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrieveAllPostsForUser (@PathVariable Integer id) {
		 Optional<User> user = userRepository.findById(id);
		 if (!user.isPresent()) 
				throw new UserNotFoundException("id-"+id);
		 return user.get().getPosts();
	}
	
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<Object> creatPost(@PathVariable Integer id, @RequestBody Post post) {
		Optional<User> userFound = userRepository.findById(id);
		if (!userFound.isPresent())
			throw new UserNotFoundException("id-" + id);

		User user = userFound.get();

		post.setUser(user);

		Post savedPost = postRepository.save(post);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedPost.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}
}
