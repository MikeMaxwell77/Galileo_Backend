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
        		.andExpect(jsonPath("$.longitude").value(118.2437));

        logger.info("testCreateBookmark passed.");
    }

    @Test
    public void testUpdateBookmark() throws Exception {
        // Arrange
    	String updatedBookmarkJson = "{"
                + "\"accountID\": 3,"
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
                .andExpect(jsonPath("$.longitude").value(1.2437));

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
