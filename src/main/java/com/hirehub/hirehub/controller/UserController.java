package com.hirehub.hirehub.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public UsersResponse createUsers(@Valid @RequestBody UserRequest UserRequest) {
	
//	    System.out.println("========== USER DATA ==========");
//	    System.out.println("Name     : " + user.getName());
//	    System.out.println("Email    : " + user.getEmail());
//	    System.out.println("Password : " + user.getPassword());
//	    System.out.println("Role     : " + user.getRole());
//	    System.out.println("===============================");

		return userService.createUser(UserRequest);

	}

	@GetMapping
	public List<UsersResponse> getAllUsers() {

        return userService.getAllUsers();
    }
	
	
	@GetMapping("/{id}")
	public UsersResponse getByid(@PathVariable Long id) {
		return userService.getusebyid(id);
		
	}

}
