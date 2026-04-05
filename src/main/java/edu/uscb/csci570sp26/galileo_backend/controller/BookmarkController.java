
package edu.uscb.csci570sp26.galileo_backend.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort; // needed for sorting list of users by ID
import org.springframework.web.bind.annotation.*;

//import edu.uscb.csci570sp26.galileo_backend.exception.BookmarkNotFoundException;
import edu.uscb.csci570sp26.galileo_backend.model.Bookmarks;
import edu.uscb.csci570sp26.galileo_backend.repository.BookmarksRepository;

@RestController
public class BookmarkController {

	@Autowired
	private BookmarksRepository bookmarksRepository;

	
	@PostMapping("/bookmark")
	Bookmarks newBookmark(@RequestBody Bookmarks newBookmark) {
		return bookmarksRepository.save(newBookmark);
	}
	
	//get all bookmarks for a single account
	@GetMapping("/bookmarks/{accountID}")
	List<Bookmarks> getAllBookmarksByAccount() {
		return bookmarksRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));// sort users by ID in ascending order
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
 	    		bookmark.setAccountID(newBookmark.getAccountID());
 	        	bookmark.setWhichAPI(newBookmark.getWhichAPI());
 				bookmark.setAPI_identifier(newBookmark.getAPI_identifier());
 				bookmark.setTimestamp(newBookmark.getTimestamp());
 				bookmark.setLatitude(newBookmark.getLatitude());
 				bookmark.setLongitude(newBookmark.getLongitude());
 			// while it isn't necessary to update the collectionID when updating a bookmark, we include it becuase updating bookmark and collection ID
 				bookmark.setCollectionID(newBookmark.getCollectionID());
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