package com.hirehub.hirehub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hirehub.hirehub.entity.Users;

public interface UserRepository  extends JpaRepository<Users, Long>{
	Optional<Users> findByEmail(String email);

}
