package edu.uscb.csci570sp26.galileo_backend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import org.hamcrest.Matchers; // easier to compare accounts for the list searches

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BookmarkControllerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(BookmarkControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private Long testBookmarkId;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
		// Insert a test bookmark into the database and retrieve its ID	
        String newBookmarkJson = "{"
                + "\"accountID\": 1,"
        		+ "\"displayName\": \"test\","
                + "\"whichAPI\": \"astronomy\","
                + "\"api_identifier\": \"api123\","
                + "\"timestamp\": 123456789,"
                + "\"latitude\": 34.0522,"
                + "\"longitude\": -118.2437"
                + "}";
        
        String response = mockMvc.perform(post("/bookmark")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newBookmarkJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract the ID from the response (assuming the response contains the Bookmark ID)
        Integer id = JsonPath.read(response, "$.id");
        testBookmarkId = id.longValue();

        logger.info("Setup complete. Test Bookmark ID: {}", testBookmarkId);
    }
    
 // GET /bookmarks/{accountID} — owner view, returns ALL bookmarks for the account
    @Test
    public void testGetBookmarksbyAccountID() throws Exception {
        // Arrange
        logger.info("Testing getBookmarksbyAccountID (should return all bookmarks regardless of account privacy)");

        // Seed a second bookmark for accountID 1
        String secondBookmarkJson = "{"
                + "\"accountID\": 1,"
                + "\"displayName\": \"test\","
                + "\"whichAPI\": \"astronomy\","
                + "\"api_identifier\": \"api456\","
                + "\"timestamp\": 999999999,"
                + "\"latitude\": 10.0,"
                + "\"longitude\": -10.0"
                + "}";

        mockMvc.perform(post("/bookmark")
                .contentType(MediaType.APPLICATION_JSON)
                .content(secondBookmarkJson))
                .andExpect(status().isOk());

        // Act & Assert — all bookmarks for account 1 should appear
        mockMvc.perform(get("/bookmarks/{accountID}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(2)))
                .andExpect(jsonPath("$[*].accountID",
                        org.hamcrest.Matchers.everyItem(org.hamcrest.Matchers.equalTo(1))));

        logger.info("testGetBookmarksbyAccountID passed.");
    }
    
    
    // BOOKMARK BY ACCOUNT SEARCHES
    // GET /bookmarks/search/{accountID} — public search, only returns bookmarks
    //         belonging to accounts where privacy = false
    @Test
    public void testGetAllBookmarksByAccountPublicOnly() throws Exception {
        // Arrange — accountID 1 is assumed to be a public account (privacy = false)
        logger.info("Testing getAllBookmarksByAccount public search — account 1 is public, results should appear");

        // Act & Assert — bookmarks for a public account should be returned
        mockMvc.perform(get("/bookmarks/search/{accountID}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$[*].accountID",
                        org.hamcrest.Matchers.everyItem(org.hamcrest.Matchers.equalTo(1))));

        logger.info("testGetAllBookmarksByAccountPublicOnly passed.");
    }

    // GET /bookmarks/search/{accountID} — should return 404 when account is private
    @Test
    public void testGetBookmarksByAccountPublicSearch_privateAccount_returns404() throws Exception {
        // Arrange — create a private account and a bookmark under it
        // (assumes you have an endpoint to create accounts, adjust the path as needed)
        String privateAccountJson = "{"
                + "\"email\": \"privateAccount\","
        		+ "\"password\": \"password123\","
                + "\"privacy\": \"true\""
                + "}";

        String accountResponse = mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(privateAccountJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Integer privateAccountId = JsonPath.read(accountResponse, "$.id");
        logger.info("Created private account with ID: {}", privateAccountId);

        // Act & Assert — searching a private account's bookmarks should be blocked
        mockMvc.perform(get("/bookmarks/search/{accountID}", privateAccountId))
                .andExpect(status().isNotFound());

        logger.info("testGetBookmarksByAccountPublicSearch_privateAccount_returns404 passed.");
    }
    
    
    // BOOKMARK SEARCHES
    @Test
    public void testGetBookmarkById() throws Exception {
        // Arrange
        logger.info("Testing getBookmarkById with ID: {}", testBookmarkId);

        // Act & Assert
        mockMvc.perform(get("/bookmark/{id}", testBookmarkId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testBookmarkId));

        logger.info("testGetBookmarkById passed.");
    }
    
    @Test
    public void testGetAllBookmarksByAccount() throws Exception {
		// Arrange
		logger.info("Testing getAllBookmarksByAccount for accountID: 1");

		// Act & Assert
		mockMvc.perform(get("/bookmarks/{accountID}", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].accountID").value(1));

		logger.info("testGetAllBookmarksByAccount passed.");
	}

    @Test
    public void testCreateBookmark() throws Exception {
        // Arrange
    	String newBookmarkJson = "{"
                + "\"accountID\": 3,"
                + "\"displayName\": \"test\","
                + "\"whichAPI\": \"ASstronomy\","
                + "\"api_identifier\": \"api1234\","
                + "\"timestamp\": 123456969,"
                + "\"latitude\": -34.0522,"
                + "\"longitude\": 118.2437"
                + "}";
        logger.info("Testing createBookmark with payload: {}", newBookmarkJson);

        // Act & Assert
        mockMvc.perform(post("/bookmark")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newBookmarkJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountID").value((long) 3))
                .andExpect(jsonPath("$.whichAPI").value("ASstronomy"))
                .andExpect(jsonPath("$.api_identifier").value("api1234"))
        		.andExpect(jsonPath("$.timestamp").value(123456969))
        		.andExpect(jsonPath("$.latitude").value(-34.0522))
        		.andExpect(jsonPath("$.longitude").value(118.2437))
        		.andExpect(jsonPath("$.displayName").value("test"));

        logger.info("testCreateBookmark passed.");
    }

    @Test
    public void testUpdateBookmark() throws Exception {
        // Arrange
    	String updatedBookmarkJson = "{"
                + "\"accountID\": 3,"
                + "\"displayName\": \"test\","
                + "\"whichAPI\": \"AstronomyThree\","
                + "\"api_identifier\": \"anotherAPI\","
                + "\"timestamp\": 123,"
                + "\"latitude\": -43.0522,"
                + "\"longitude\": 1.2437"
                + "}";
    	
        logger.info("Testing updateBookmark with payload: {}", updatedBookmarkJson);

        // Act & Assert
        mockMvc.perform(put("/bookmark/{id}", testBookmarkId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedBookmarkJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountID").value((long) 3))
                .andExpect(jsonPath("$.whichAPI").value("AstronomyThree"))
                .andExpect(jsonPath("$.api_identifier").value("anotherAPI"))
                .andExpect(jsonPath("$.timestamp").value(123))
                .andExpect(jsonPath("$.latitude").value(-43.0522))
                .andExpect(jsonPath("$.longitude").value(1.2437))
                .andExpect(jsonPath("$.displayName").value("test"));

        logger.info("testUpdateBookmark passed.");
    }

    @Test
    public void testDeleteBookmark() throws Exception {
        // Arrange
        logger.info("Testing deleteBookmark with ID: {}", testBookmarkId);

        // Act & Assert
        mockMvc.perform(delete("/bookmark/{id}", testBookmarkId))
                .andExpect(status().isOk())
                .andExpect(content().string("Bookmark with id " + testBookmarkId + " has been deleted successfully."));

        logger.info("testDeleteBookmark passed.");
    }
}
