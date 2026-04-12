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
public class AccountControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(AccountControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private Long testAccountId;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        // Insert a test user into the database and retrieve its ID
        String newAccountJson = "{\"email\":\"johndoe@example.com\",\"password\":\"password\"}";
        String response = mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newAccountJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract the ID from the response (assuming the response contains the Account ID)
        Integer id = JsonPath.read(response, "$.id");
        testAccountId = id.longValue();

        logger.info("Setup complete. Test Account ID: {}", testAccountId);
    }

    @Test
    public void testGetAccountById() throws Exception {
        // Arrange
        logger.info("Testing getAccountById with ID: {}", testAccountId);

        // Act & Assert
        mockMvc.perform(get("/account/{id}", testAccountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testAccountId));

        logger.info("testGetAccountById passed.");
    }

    @Test
    public void testCreateAccount() throws Exception {
        // Arrange
        String newAccountJson = "{\"email\":\"johndoe2@example.com\",\"password\":\"password\"}";
        logger.info("Testing createAccount with payload: {}", newAccountJson);

        // Act & Assert
        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newAccountJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("johndoe2@example.com"))
                .andExpect(jsonPath("$.password").value("password"));

        logger.info("testCreateAccount passed.");
    }

    @Test
    public void testUpdateAccount() throws Exception {
        // Arrange
    	String updatedAccountJson = "{\"email\":\"janedoe2@example.com\",\"password\":\"passwordA\"}";
        logger.info("Testing updateAccount with payload: {}", updatedAccountJson);

        // Act & Assert
        mockMvc.perform(put("/account/{id}", testAccountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedAccountJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("janedoe2@example.com"))
                .andExpect(jsonPath("$.password").value("passwordA"));

        logger.info("testUpdateAccount passed.");
    }

    @Test
    public void testDeleteAccount() throws Exception {
        // Arrange
        logger.info("Testing deleteAccount with ID: {}", testAccountId);

        // Act & Assert
        mockMvc.perform(delete("/account/{id}", testAccountId))
                .andExpect(status().isOk())
                .andExpect(content().string("Account with id " + testAccountId + " has been deleted successfully."));

        logger.info("testDeleteAccount passed.");
    }
   
}