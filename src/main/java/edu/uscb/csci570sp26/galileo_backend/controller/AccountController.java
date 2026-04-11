package edu.uscb.csci570sp26.galileo_backend.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort; // needed for sorting list of users by ID
import org.springframework.web.bind.annotation.*;

//import edu.uscb.csci570sp26.galileo_backend.exception.UserNotFoundException;
import edu.uscb.csci570sp26.galileo_backend.model.Accounts;
import edu.uscb.csci570sp26.galileo_backend.repository.AccountsRepository;


@RestController
public class AccountController {


	@Autowired
	private AccountsRepository accountsRepository;


	@PostMapping("/account")
	Accounts newAccount(@RequestBody Accounts newAccount) {
		return accountsRepository.save(newAccount);
	}

	//was in the demo project, but we might want this skeleton for search functionality later on
	/*
	@GetMapping("/accounts")
	List<Accounts> getAllAccounts() {
		return accountsRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));// sort users by ID in ascending order
	}
	*/


	@GetMapping("/account/{id}")
	Accounts getAccountById(@PathVariable Long id) {
		return accountsRepository.findById(id)
 	    	.orElseThrow(() -> new RuntimeException("Account not found with id " + id));
	}

	@GetMapping("/accounts/search")
	List<Accounts> searchByUsername(@RequestParam String username) {
		return accountsRepository.findByUsernameContainingIgnoreCase(username);
	}

	@PutMapping("/account/{id}")
	Accounts updateAccount(@RequestBody Accounts newAccount, @PathVariable Long id) {
		return accountsRepository.findById(id)
 	    	.map(account -> {
 	    		account.setEmail(newAccount.getEmail());
 	        	account.setUsername(newAccount.getUsername());
 				account.setPassword(newAccount.getPassword());
 				return accountsRepository.save(account);
 	        }).orElseThrow(() -> new RuntimeException("Account not found with id " + id));
	}



	@DeleteMapping("/account/{id}")
	String deleteAccount(@PathVariable Long id) {
		if(!accountsRepository.existsById(id)) {
 			throw new RuntimeException("Account not found with id " + id);
 		}
	 	accountsRepository.deleteById(id);
	 	return "Account with id " + id + " has been deleted successfully.";

	}
}