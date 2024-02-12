package com.gdpdemo.GDPSprint1Project.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gdpdemo.GDPSprint1Project.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
