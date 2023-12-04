package com.gdpdemo.GDPSprint1Project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.gdpdemo.GDPSprint1Project.Home;



public interface HomeRepository extends JpaRepository<Home, Integer>{
	
	@Query("select u from Home u where u.email = :email")
	public Home getUserByUserName(@Param("email") String email);
	
}
