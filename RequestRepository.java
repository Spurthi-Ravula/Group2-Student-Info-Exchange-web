package com.example.SMS.repository;

import com.example.SMS.entity.Request;
import com.example.SMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

	List<Request> findAllByUser(User user);
	
	Optional<Request> getRequestByUserAndId(User user, Long id);
}