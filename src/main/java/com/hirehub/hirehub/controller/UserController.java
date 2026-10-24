package com.hirehub.hirehub.controller;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hirehub.hirehub.dto.UserRequest;

import com.hirehub.hirehub.responseDto.UsersResponse;
import com.hirehub.hirehub.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<UsersResponse> createUsers(@Valid @RequestBody UserRequest UserRequest) {
	
//	    System.out.println("========== USER DATA ==========");
//	    System.out.println("Name     : " + user.getName());
//	    System.out.println("Email    : " + user.getEmail());
//	    System.out.println("Password : " + user.getPassword());
//	    System.out.println("Role     : " + user.getRole());
//	    System.out.println("===============================");

		 UsersResponse userResponse=userService.createUser(UserRequest);
		 return ResponseEntity
				 .status(HttpStatus.CREATED)
				 .body(userResponse);

	}

//	@GetMapping
//	public List<UsersResponse> getAllUsers() {
//        return userService.getAllUsers();
//    }
	
	@GetMapping
	public Page<UsersResponse> getAllUsers(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "id") String sortBy,
	        @RequestParam(defaultValue = "asc") String direction) {

	    return userService.getAllUsers(page, size, sortBy, direction);
	}
	
	
	@GetMapping("/{id}")
	public UsersResponse getByid(@PathVariable Long id) {
		return userService.getUsersById(id);
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<UsersResponse> updateById(@PathVariable @RequestBody  UserRequest userRequest,Long id) {
		UsersResponse updatedUser= userService.updateById(userRequest,id);
		return ResponseEntity.ok(updatedUser);
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id){
		userService.deleteById(id);
		return ResponseEntity.noContent().build();
		
	}
	public ResponseEntity<Void> deleteAll(){
		userService.deletAll();
		return ResponseEntity.notFound().build();
	}
}
