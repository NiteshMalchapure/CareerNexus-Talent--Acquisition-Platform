package com.hirehub.hirehub.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hirehub.hirehub.ExceptionHandler.EmailAlreradyExist;
import com.hirehub.hirehub.ExceptionHandler.UserNotFoundException;
import com.hirehub.hirehub.dto.UserRequest;
import com.hirehub.hirehub.entity.Users;	
import com.hirehub.hirehub.repository.UserRepository;
import com.hirehub.hirehub.responseDto.UsersResponse;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService																;

    // Test 1: Successfully create a user
    @Test
    void createUser_shouldReturnUserResponse() {

        UserRequest request = new UserRequest();
        request.setName("Rahul");
        request.setEmail("rahul@example.com");
        request.setPassword("Secret123!");
        request.setRole("USER");
       

        when(userRepository.findByEmail("rahul@example.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("Secret123!"))
                .thenReturn("encoded-password");

//        when(userRepository.save(any(Users.class)))
//                .thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.save(any(Users.class)))
        .thenAnswer(invocation -> {
            Users user = invocation.getArgument(0);
            user.setId(1L); 
            return user;
        });
        
        UsersResponse response = userService.createUser(request);

        assertEquals("Rahul", response.getName());
        assertEquals("rahul@example.com", response.getEmail());
        assertEquals("USER", response.getRole());

        verify(passwordEncoder).encode("Secret123!");
        verify(userRepository).save(any(Users.class));
    }

   
	

	// Test 2: Reject duplicate email
    
    @Test
    void createUser_shouldThrowExceptionForDuplicateEmail() {

        UserRequest request = new UserRequest();
        request.setName("Rahul");
        request.setEmail("rahul@example.com");
        request.setPassword("Secret123!");
        request.setRole("USER");

        Users existingUser = new Users();
        existingUser.setEmail("rahul@example.com");	

        when(userRepository.findByEmail("rahul@example.com"))
                .thenReturn(Optional.of(existingUser));

        assertThrows(
        		EmailAlreradyExist.class,
                () -> userService.createUser(request)
        );

        verify(userRepository, never()).save(any(Users.class));
    }

    // Test 3: User not found
    
    @Test
    void getUserById_shouldThrowExceptionWhenUserDoesNotExist() {

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
        		UserNotFoundException.class,
                () -> userService.getUsersById(99L)
        );

        verify(userRepository).findById(99L);
    }
    @Test
    void updateUser_shouldUpdateExistingUser() {

        Long userId = 1L;

        Users existingUser = new Users();
        existingUser.setId(userId);
        existingUser.setName("Old Name");
        existingUser.setEmail("old@example.com");
        existingUser.setPassword("old-encoded-password");
        existingUser.setRole("USER");

        UserRequest request = new UserRequest();
        request.setName("New Name");
        request.setEmail("new@example.com");
        request.setPassword("NewSecret123!");
        request.setRole("ADMIN");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existingUser));

        when(userRepository.findByEmail("new@example.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("NewSecret123!"))
                .thenReturn("new-encoded-password");

        when(userRepository.save(any(Users.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UsersResponse response = userService.updateById(request, userId);

        assertEquals("New Name", response.getName());
        assertEquals("new@example.com", response.getEmail());
        assertEquals("ADMIN", response.getRole());

        assertEquals("new-encoded-password", existingUser.getPassword());

        verify(userRepository).save(existingUser);
        verify(passwordEncoder).encode("NewSecret123!");
    }
    
    @Test
    void deleteUser_shouldDeleteExistingUser() {

        Long userId = 1L;

        Users existingUser = new Users();
        existingUser.setId(userId);
        existingUser.setName("Rahul");
        existingUser.setEmail("rahul@example.com");
        existingUser.setPassword("encoded-password");
        existingUser.setRole("USER");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existingUser));

        userService.deleteById(userId);

        verify(userRepository).findById(userId);
        verify(userRepository).delete(existingUser);
    }
    
    @Test
    void deleteUser_shouldThrowExceptionWhenUserDoesNotExist() {

        Long userId = 999L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteById(userId)
        );

        verify(userRepository, never()).delete(any(Users.class));
    }

}
