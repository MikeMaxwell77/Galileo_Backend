package edu.uscb.csci570sp26.galileo_backend.model;

import jakarta.persistence.*;

@Entity  // Tells JPA to create a table for this class
@Table(name = "Accounts")  // Specifies the table name in the database
public class Accounts {

	@Id  // Marks this field as the primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generates the ID
	private Long id;
	
	@Column(nullable = false)  // Ensures password is not null
	private String password;
	
	@Column(nullable = false, unique = true)  // Ensures email is unique and not null
	private String email;
	
	@Column(nullable = false)  // Ensures role is not null
	private boolean privacy = false;  // default we can search them

	// Constructors
	public Accounts() {
		
	};
	public Accounts(String email, String password) {
		this.email = email;
		this.password = password;
	}

	
	// Getters and setters for all fields	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isPrivacy() {
		return privacy;
	}

	public void setPrivacy(boolean privacy) {
		this.privacy = privacy;
	}
	
	
	
	
}
