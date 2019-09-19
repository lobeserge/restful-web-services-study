package com.example.demo.user;

import static  org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import java.net.URI;
import java.util.List;

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
public class UserResource {

	@Autowired
	private UserDaoService service;
	//retreive all users
	
	@GetMapping(path="/users")
	public List<User> retrieveAllUser(){
		
		return service.findAll();
	}
	
	@GetMapping(path="/users/{id}")
	public Resource<User> retrieveOneUser(@PathVariable int id){
		
		User user= service.findOne(id);
		if(user==null) 
			throw new UserNotFoundException("id-"+id+" not found");
		
		  Resource<User> resource=new Resource<User>(user);
		  ControllerLinkBuilder LinkTo=linkTo(methodOn(this.getClass()).retrieveAllUser());
		  resource.add(LinkTo.withRel("all-users"));
				  
				  
		  return resource;
		
	}
	
	@PostMapping(path="/users")
	public ResponseEntity<Object> CreateUser(@Valid @RequestBody User user  ){
		User newuser=service.save(user);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newuser.getId()).toUri();
	
		
		return ResponseEntity.created(location).build();
		
		
	}
	
	@DeleteMapping(path="/users/{id}")
	public void DeleteUser(@PathVariable int id){
		
		User user= service.DeleteUser(id);
		if(user==null) 
			throw new UserNotFoundException("id-"+id+" not found");
			
	}
	
	

}
