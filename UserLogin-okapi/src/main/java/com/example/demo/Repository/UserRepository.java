package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity,Long>{


	UserEntity findByUsername(String username);

	UserEntity findByUserId(String userId);

	UserEntity findByMobile(String mobile);

	UserEntity findByEmail(String email);
}
