
package edu.uscb.csci570sp26.galileo_backend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional; // Optional is used to avoid NullPointerExceptions

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import edu.uscb.csci570sp26.galileo_backend.model.Accounts;
import edu.uscb.csci570sp26.galileo_backend.repository.AccountsRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AccountsRepositoryTest{

	@Autowired
	private AccountsRepository accountsRepository;

	@Test
	public void testFindById() {
		// Arrange
		Accounts account = new Accounts();
		account.setUsername("testuser");
		account.setPassword("testpassword");
		account = accountsRepository.save(account);

		// Act
		Optional<Accounts> foundAccount = accountsRepository.findById(account.getID());
		
		// Assert
		assertTrue(foundAccount.isPresent());
		assertEquals("testuser", foundAccount.get().getUsername());
	}
	
	@Test
	public void testSave() {
		// Arrange
		Accounts account = new Accounts();
		account.setUsername("newuser");
		account.setPassword("newpassword");

		// Act
		Accounts savedAccount = accountsRepository.save(account);

		// Assert
		assertEquals("newuser", savedAccount.getUsername());
		assertTrue(accountsRepository.findById(savedAccount.getID()).isPresent());
	}
	
	@Test
	public void testDeleteById() {
		// Arrange
		Accounts account = new Accounts();
		account.setUsername("deleteuser");
		account.setPassword("deletepassword");
		account = accountsRepository.save(account);

		// Act
		accountsRepository.deleteById(account.getID());

		// Assert
		assertFalse(accountsRepository.findById(account.getID()).isPresent());
	}

}
