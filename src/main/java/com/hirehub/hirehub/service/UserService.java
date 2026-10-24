package com.hirehub.hirehub.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

/// 	CREATE USERE ACCOUNT
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
	
//	public List<UsersResponse> getAllUsers() {
//		List<Users> user=userRepository.findAll();
//		
//		List<UsersResponse> responses= new ArrayList<>();
//		for(Users users:user) {
//			
//			UsersResponse response= new UsersResponse();
//			
//			response.setId(users.getId());
//			response.setName(users.getName());
//			response.setEmail(users.getEmail());
//			response.setRole(users.getRole());
//			
//			responses.add(response);
//		}
//		return responses;
//		
//	}
	/// GET ALL THE USERS(USING PAGINATION AND SORTING)
	public Page<UsersResponse> getAllUsers(
	        int page,
	        int size,
	        String sortBy,
	        String direction) {
		
		 if(page<0) {
			 throw new IllegalArgumentException("Page no cannot be negative");
		 }
		 
		 if(size<1||size>100) {
			 throw new IllegalArgumentException("Size should must be between 1 and 100");
		 }
		 List<String> allowedSortFields =
		            List.of("id", "name", "email", "role");
		 
		 if(!allowedSortFields.contains(sortBy)) {
			 throw new IllegalArgumentException("Invalid sort filed.Allowed field: id ,name,email,role");
		 }
		 
		 if(!direction.equalsIgnoreCase("asc")&& !direction.equalsIgnoreCase("desc")) {
			 throw new IllegalArgumentException("Direction must be either asc or desc");
		 }
		 
// Converting --> Direction Strig to Spring sort.Direction enum
//	    Sort.Direction sortDirection =
//	            Sort.Direction.fromString(direction);
		// we can also user insted of ---->TERNARY(  ? : )Operator 
		 
//		 if (direction.equalsIgnoreCase("desc")) {
//			    sort = Sort.by(sortBy).descending();
//			} else {
//			    sort = Sort.by(sortBy).ascending();
//			}
	    
	Sort sort=direction.equalsIgnoreCase("desc")
			  ? Sort.by(sortBy).descending()
			  : Sort.by(sortBy).ascending();


	    Pageable pageable = PageRequest.of(
	            page,
	            size,
	            sort
	    );
	    
//	    ///Container retured by DataBase
//	    Page<Users> usersPage =
//	            userRepository.findAll(pageable);
	    
	  return  userRepository.findAll(pageable)
		  
			  .map(user -> {
	        UsersResponse response = new UsersResponse();

	        response.setId(user.getId());
	        response.setName(user.getName());
	        response.setEmail(user.getEmail());
	        response.setRole(user.getRole());

	        return response;
	    });
	}
	//* .map(user -> ...)	->   Converts each Users entity into a UserResponse,
//    keeping password hashes out of the API response. *//
	   
//    return usersPage.map(user -> {
	
	/// GET USER BY ID
	public UsersResponse getUsersById(Long id) {
		
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

// UPDATE BY ID 
	public UsersResponse updateById(UserRequest userRequest, Long id) {
		Users users=userRepository.findById(id)
		.orElseThrow(()->
		new UserNotFoundException("user in not foud with this id"+ id));
		
		users.setEmail(userRequest.getEmail());
		users.setName(userRequest.getName());
		
		String encodedpassword=passwordEncoder.encode(userRequest.getPassword());
		users.setPassword(encodedpassword);
		
		users.setRole(userRequest.getRole());
		
	Users updatedUsers	=userRepository.save(users);
		
		 UsersResponse response =new UsersResponse();
		 response.setId(updatedUsers.getId());
		response.setEmail(updatedUsers.getEmail()); 
		response.setName(updatedUsers.getName());
		response.setRole(updatedUsers.getRole());
		
		return response;
	}
	
//	DELET BY ID 
	
	public void deleteById(Long id) {
		userRepository.findById(id)
		.orElseThrow(()->
		new UserNotFoundException("user in not foud with this id"+ id));
		userRepository.deleteById(id);	
	}
	
//	DELETE ALL
	
	public void deletAll() {
		userRepository.deleteAll();
	}
}