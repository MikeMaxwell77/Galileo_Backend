
package edu.uscb.csci570sp26.galileo_backend.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort; // needed for sorting list of users by ID
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

//import edu.uscb.csci570sp26.galileo_backend.exception.BookmarkNotFoundException;
import edu.uscb.csci570sp26.galileo_backend.model.Accounts;
import edu.uscb.csci570sp26.galileo_backend.model.Bookmarks;
import edu.uscb.csci570sp26.galileo_backend.repository.AccountsRepository;
import edu.uscb.csci570sp26.galileo_backend.repository.BookmarksRepository;

@RestController
public class BookmarkController {

	@Autowired
	private BookmarksRepository bookmarksRepository;
	@Autowired
	private AccountsRepository accountsRepository;

	
	@PostMapping("/bookmark")
	Bookmarks newBookmark(@RequestBody Bookmarks newBookmark) {
		return bookmarksRepository.save(newBookmark);
	}
	
	//get all bookmarks for a single account
	@GetMapping("/bookmarks/search/{accountID}")
	List<Bookmarks> getAllBookmarksByAccount(@PathVariable Long accountID) {
		boolean privateAccount = accountsRepository.findById(accountID)
				.map(Accounts::isPrivacy)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")); // this occurs if they are not found
		
		if (privateAccount ) {// false is default, we want them to be seen
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
		
		return bookmarksRepository.findByAccountID(accountID);// sort users by ID in ascending order
	}
	
	@GetMapping("/bookmarks/{accountID}")
	List<Bookmarks> getAllMyBookmarksByAccount(@PathVariable Long accountID) {
		if (!accountsRepository.existsById(accountID)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
		
		return bookmarksRepository.findByAccountID(accountID);// sort users by ID in ascending order
	}
	

	
	@GetMapping("/bookmark/{id}")
	Bookmarks getBookmarkById(@PathVariable Long id) {
		return bookmarksRepository.findById(id)
 	    	.orElseThrow(() -> new RuntimeException("Bookmark not found with id " + id));
	}

	@PutMapping("/bookmark/{id}")
	Bookmarks updateBookmark(@RequestBody Bookmarks newBookmark, @PathVariable Long id) {
		return bookmarksRepository.findById(id)
 	    	.map(bookmark -> {
    		    /*if (newBookmark.getAccountID() != null) {
    		        bookmark.setAccountID(newBookmark.getAccountID());
    		    }*/ //we don't need to adjust the bookmarkID
    		    if (newBookmark.getWhichAPI() != null) {
    		        bookmark.setWhichAPI(newBookmark.getWhichAPI());
    		    }
    		    if (newBookmark.getAPI_identifier() != null) {
    		        bookmark.setAPI_identifier(newBookmark.getAPI_identifier());
    		    }
    		    if (newBookmark.getTimestamp() != 0L) {
    		        bookmark.setTimestamp(newBookmark.getTimestamp());
    		    }
    		    if (newBookmark.getLatitude() != 0) {
    		        bookmark.setLatitude(newBookmark.getLatitude());
    		    }
    		    if (newBookmark.getLongitude() != 0) {
    		        bookmark.setLongitude(newBookmark.getLongitude());
    		    }
    		    if (newBookmark.getCollectionID() != null) {
    		        bookmark.setCollectionID(newBookmark.getCollectionID());
    		    }
    		    if (newBookmark.getDisplayName() != null) {
    		        bookmark.setDisplayName(newBookmark.getDisplayName());
    		    }
    		    if (newBookmark.getDate() != 0L) {
    		        bookmark.setDate(newBookmark.getDate());
    		    }

    		    return bookmarksRepository.save(bookmark);
 	        }).orElseThrow(() -> new RuntimeException("Bookmark not found with id " + id));
	}
	

	
	@DeleteMapping("/bookmark/{id}")
	String deleteBookmark(@PathVariable Long id) {
		if(!bookmarksRepository.existsById(id)) {
 			throw new RuntimeException("Bookmark not found with id " + id);
 		}
	 	bookmarksRepository.deleteById(id);
	 	return "Bookmark with id " + id + " has been deleted successfully.";

	}

} // end class BookmarkController