package com.example.demo.model;

import javax.persistence.*;
import javax.persistence.Id;

public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", nullable = false)
	private String name;

    // Constructors
	public Customer(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

    // Getters and Setters
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getCustName() { return name; }
	public void setCustName(String name) { this.name = name; }

	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + "]";
	}
}
