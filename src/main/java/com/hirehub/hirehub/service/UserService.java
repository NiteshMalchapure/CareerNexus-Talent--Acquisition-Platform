package com.hirehub.hirehub.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.hirehub.hirehub.ExceptionHandler.EmailAlreradyExist;
import com.hirehub.hirehub.ExceptionHandler.UserNotFoundException;
import com.hirehub.hirehub.dto.UserRequest;
import com.hirehub.hirehub.entity.Users;
import com.hirehub.hirehub.repository.UserRepository;
import com.hirehub.hirehub.responseDto.UsersResponse;

import jakarta.validation.Valid;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}


	public UsersResponse createUser(@Valid UserRequest userRequest) {
		if(userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
			throw new EmailAlreradyExist("Email already exists");
		}
		
		//** This occurs between the layer**//
		//** UserRequestDto -----> ENTITY **//  
		
		Users user = new Users();
		user.setName(userRequest.getName());
		
		user.setEmail(userRequest.getEmail());
		
		
		String encodePassword=passwordEncoder.encode(userRequest.getPassword());
		user.setPassword(encodePassword);
		user.setRole(userRequest.getRole());
		
		 // Save entity--> bcz --> { after saving only database generate an "id" }
		Users savedUser	 = userRepository.save(user);
		
		
	//** ENTITY --------> UserRresponseDto **//
		
 //// to give the out put for postman
		
		UsersResponse response=new UsersResponse();
		response.setId(savedUser.getId());
		response.setName(savedUser.getName());
		response.setEmail(savedUser.getEmail());
		response.setRole(savedUser.getRole());
		
		 return (response);
		
	}
		
	public List<UsersResponse> getAllUsers() {
		List<Users> user=userRepository.findAll();
		
		List<UsersResponse> responses= new ArrayList<>();
		for(Users users:user) {
			
			UsersResponse response= new UsersResponse();
			
			response.setId(users.getId());
			response.setName(users.getName());
			response.setEmail(users.getEmail());
			response.setRole(users.getRole());
			
			responses.add(response);
		}
		return responses;
		
	}
	
	public UsersResponse getusebyid(Long id) {
		
	    Users user =userRepository.findById(id)
	    		 .orElseThrow(() ->
                 new UserNotFoundException(
                         "User not found with id: " + id
                 )
         );
	    
	    UsersResponse response =new  UsersResponse();
	    response.setId(user.getId());
	    response.setEmail(user.getEmail());
	    response.setName(user.getName());
	    response.setRole(user.getRole());
	    
	    return response;
		}

}