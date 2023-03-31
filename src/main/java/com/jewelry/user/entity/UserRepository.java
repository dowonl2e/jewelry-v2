package com.jewelry.user.entity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String>{

    Optional<UserEntity> findByUserId(String userid);
	
}
