package com.gdpdemo.GDPSprint1Project;
	@Entity
	@Table(name = "products")
	public class Product {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String name;
	    private double price;
	    private String description;

	    // Constructors, getters, setters

}
