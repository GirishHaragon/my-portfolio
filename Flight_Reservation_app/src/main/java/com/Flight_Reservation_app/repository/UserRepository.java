package com.Flight_Reservation_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Flight_Reservation_app.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);//Here we can write any method name that should follow the spring naming convention like getByEmail(), readByEmail(), everything works the same where these methods are not already present in JPArepository.
	
}