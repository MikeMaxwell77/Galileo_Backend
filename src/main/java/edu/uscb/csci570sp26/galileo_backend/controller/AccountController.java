package edu.uscb.csci570sp26.galileo_backend.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort; // needed for sorting list of users by ID
import org.springframework.web.bind.annotation.*;

//import edu.uscb.csci570sp26.galileo_backend.exception.UserNotFoundException;
import edu.uscb.csci570sp26.galileo_backend.model.Accounts;
import edu.uscb.csci570sp26.galileo_backend.repository.AccountsRepository;
import edu.uscb.csci570sp26.galileo_backend.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder; // update account password

@RestController
public class AccountController {


	@Autowired
	private AccountsRepository accountsRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtil jwtUtil;

	//was in the demo project, but we might want this skeleton for search functionality later on
		/*
	@PostMapping("/account")
	Accounts newAccount(@RequestBody Accounts newAccount) {
		return accountsRepository.save(newAccount);
	}

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
	
	@GetMapping("/account/me")
	Accounts getCurrentAccount(@RequestHeader("Authorization") String authHeader) {

	    String token = authHeader.replace("Bearer ", "");
		Long userId = jwtUtil.extractUserId(token);

	    return accountsRepository.findById(userId)
	        .orElseThrow(() -> new RuntimeException("Account not found"));
	}
	
	@GetMapping("/accounts/search")
	List<Accounts> searchByEmail(@RequestParam String email) {
		return accountsRepository.findByEmailContainingIgnoreCaseAndPrivacyFalse(email);
	}

	/*
	@PutMapping("/account/{id}")
	Accounts updateAccount(@RequestBody Accounts newAccount, @PathVariable Long id) {
		return accountsRepository.findById(id)
 	    	.map(account -> {
 	    		account.setEmail(newAccount.getEmail());
 				account.setPassword(newAccount.getPassword());
 				return accountsRepository.save(account);
 	        }).orElseThrow(() -> new RuntimeException("Account not found with id " + id));
	}
	*/
	
	@PutMapping("/account/me")
	Accounts updateCurrentAccount(
	    @RequestBody Accounts newAccount,
	    @RequestHeader("Authorization") String authHeader
	) {
	    String token = authHeader.replace("Bearer ", "");
	    Long userId = jwtUtil.extractUserId(token);

	    return accountsRepository.findById(userId)
	    		.map(account -> {
	    			account.setPassword(passwordEncoder.encode(newAccount.getPassword()));
	    			account.setPrivacy(newAccount.isPrivacy());
	    			return accountsRepository.save(account);
	    		}).orElseThrow(() -> new RuntimeException("Account not found"));
	}


	/*
	@DeleteMapping("/account/{id}")
	String deleteAccount(@PathVariable Long id) {
		if(!accountsRepository.existsById(id)) {
 			throw new RuntimeException("Account not found with id " + id);
 		}
	 	accountsRepository.deleteById(id);
	 	return "Account with id " + id + " has been deleted successfully.";

	}
	*/
	
	@DeleteMapping("/account/me")
	String deleteCurrentAccount(@RequestHeader("Authorization") String authHeader) {

	    String token = authHeader.replace("Bearer ", "");
	    Long userId = jwtUtil.extractUserId(token);

	    if (!accountsRepository.existsById(userId)) {
	        throw new RuntimeException("Account not found");
	    }

	    accountsRepository.deleteById(userId);

	    return "Account with id " + userId + " has been deleted successfully.";
	}
}