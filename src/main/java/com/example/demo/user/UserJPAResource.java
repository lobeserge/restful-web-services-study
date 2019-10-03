package com.example.demo.user;

import static  org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserJPAResource {

	@Autowired
	private UserDaoService service;
	@Autowired
	private UserRepository userReposity;
	@Autowired
	private PostRepository postRepository;
	//retreive all users
	
	@GetMapping(path="/jpa/users")
	public List<User> retrieveAllUser(){
		
		return userReposity.findAll();
	}
	
	@GetMapping(path="/jpa/users/{id}")
	public Resource<User> retrieveOneUser(@PathVariable int id){
		
		Optional<User> user= userReposity.findById(id);
		if(!user.isPresent()) 
			throw new UserNotFoundException("id-"+id+" not found");
		
		  Resource<User> resource=new Resource<User>(user.get());
		  ControllerLinkBuilder LinkTo=linkTo(methodOn(this.getClass()).retrieveAllUser());
		  resource.add(LinkTo.withRel("all-users"));
				  
				  
		  return resource;
		
	}
	
	@PostMapping(path="/jpa/users")
	public ResponseEntity<Object> CreateUser(@Valid @RequestBody User user  ){
		User newuser=userReposity.save(user);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newuser.getId()).toUri();
	
		
		return ResponseEntity.created(location).build();
		
		
	}
	
	
	
	@DeleteMapping(path="/jpa/users/{id}")
	public void DeleteUser(@PathVariable int id){
		
		userReposity.deleteById(id);
		
			
	}
	
	
	@GetMapping(path="/jpa/users/{id}/posts")
	public List<Post> retrieveAllUsers(@PathVariable int id){
		Optional<User> user= userReposity.findById(id);
		if(!user.isPresent()) 
			throw new UserNotFoundException("id-"+id+" not found");
		
		return user.get().getPost();
		
	}
	
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<Object> createPost(@PathVariable int id, @RequestBody Post post) {
		
		Optional<User> userOptional = userReposity.findById(id);
		
		if(!userOptional.isPresent()) {
			throw new UserNotFoundException("id-" + id);
		}

		User user = userOptional.get();
		
		post.setUser(user);
		
		postRepository.save(post);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId())
				.toUri();

		return ResponseEntity.created(location).build();

	}
	
	

}
