package com.basava.rest.webservices.restfulwebservices;

import java.net.URI;
import java.util.List;

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

import com.basava.rest.webservices.restfulwebservices.dao.UserDaoService;
import com.basava.rest.webservices.restfulwebservices.exception.UserNotFoundException;
import com.basava.rest.webservices.restfulwebservices.model.User;

@RestController
public class UserController {

	@Autowired
	private UserDaoService userDaoService;
	
	//retrieveAllUsers
	@GetMapping("/users")
	public List<User> retrieveAllUsers () {
		return userDaoService.findAll();
	}
	
	//retrieveAllUser(ind id)
	@GetMapping("/users/{id}")
	public EntityModel<User> retrieveOneUsers (@PathVariable Integer id) {
		User user = userDaoService.findOne(id);
		if (user == null) 
			throw new UserNotFoundException("id-"+id);
		EntityModel<User> model = EntityModel.of(user);
		WebMvcLinkBuilder linkToUser = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(this.getClass())
				.retrieveAllUsers());
		model.add(linkToUser.withRel("all-user"));
		return model;
	}
	
	
	//retrieveAllUser(ind id)
	@PostMapping("/users/")
	public ResponseEntity<Object> saveUser (@Valid @RequestBody User user) {
	  User savedUser =userDaoService.save(user);
	  URI location = ServletUriComponentsBuilder
			  .fromCurrentRequest()
			  .path("/{id}")
			  .buildAndExpand(savedUser.getId()).toUri();
	  
	  return ResponseEntity.created(location).build();
	}
	
	
	@DeleteMapping("/users/{id}")
	public void deleteUser (@PathVariable Integer id) {
	  User deletedUser =userDaoService.deleteById(id);
	  if (deletedUser == null) 
			throw new UserNotFoundException("id-"+id);
	}
}
