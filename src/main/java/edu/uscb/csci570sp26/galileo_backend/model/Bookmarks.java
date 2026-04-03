package edu.uscb.csci570sp26.galileo_backend.model;

import jakarta.persistence.*;

import java.util.GregorianCalendar;

@Entity  // Tells JPA to create a table for this class
@Table(name = "Bookmarks")  // Specifies the table name in the database
public class Bookmarks {
	
	@Id // Primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID
	private Long id;
	
	@Column(nullable = false)
	private Long accountID; // Foreign key to Accounts table
	
	@Column(nullable = false)
	private String whichAPI; // Identifier which API
	
	@Column(nullable = false)
	private String API_identifier; // Identifier within the API (e.g., planet name, star name, etc.)
	
	@Column(nullable = false) // GMT default
	private long timestamp; // Time when the bookmark was created
	
	@Column(nullable = false)
	private int latitude; // Latitude of the bookmarked location
	
	@Column(nullable = false)
	private int longitude; // Longitude of the bookmarked location
	
	private Long CollectionID; // Optional field for grouping bookmarks into collections (can be null)
	
	
	// Getters and setters for all fields
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAccountID() {
		return accountID;
	}

	public void setAccountID(Long accountID) {
		this.accountID = accountID;
	}

	public String getWhichAPI() {
		return whichAPI;
	}

	public void setWhichAPI(String whichAPI) {
		this.whichAPI = whichAPI;
	}

	public String getAPI_identifier() {
		return API_identifier;
	}

	public void setAPI_identifier(String aPI_identifier) {
		API_identifier = aPI_identifier;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	public Long getCollectionID() {
		return CollectionID;
	}

	public void setCollectionID(Long collectionID) {
		CollectionID = collectionID;
	}
	
	
}
