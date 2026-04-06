
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

import edu.uscb.csci570sp26.galileo_backend.model.Bookmarks;
import edu.uscb.csci570sp26.galileo_backend.repository.BookmarksRepository;
import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
public class BookmarksRepositoryTest{

	@Autowired
	private BookmarksRepository bookmarksRepository;

	@Test
	public void testFindById() {
		// Arrange
		Bookmarks bookmark = new Bookmarks();
		bookmark.setAccountID((long) 1);
     	bookmark.setWhichAPI("astronomy");
		bookmark.setAPI_identifier("api");
		bookmark.setTimestamp((long) 1234567890);
		bookmark.setLatitude(34.0522);
		bookmark.setLongitude(-118.2437);
		//optionally set collectionID if needed
		//bookmark.setCollectionID((long) NULL);
		bookmark = bookmarksRepository.save(bookmark);

		// Act
		Optional<Bookmarks> foundBookmark = bookmarksRepository.findById(bookmark.getId());
		
		// Assert
		assertTrue(foundBookmark.isPresent());
		assertEquals((long) 1, foundBookmark.get().getAccountID());
		assertEquals("astronomy", foundBookmark.get().getWhichAPI());
		assertEquals("api", foundBookmark.get().getAPI_identifier());
		assertEquals((long) 1234567890, foundBookmark.get().getTimestamp());
		assertEquals(34.0522, foundBookmark.get().getLatitude());
		assertEquals(-118.2437, foundBookmark.get().getLongitude());
		//optionslly assert collectionID if needed
		//assertEquals((long) NULL, foundBookmark.get().getCollectionID());
	}
	
	@Test
	public void testSave() {
		// Arrange
		Bookmarks bookmark = new Bookmarks();
		bookmark.setAccountID((long) 2);
     	bookmark.setWhichAPI("Astronomy");
		bookmark.setAPI_identifier("API");
		bookmark.setTimestamp((long) 1234567891);
		bookmark.setLatitude(-34.0522);
		bookmark.setLongitude(118.2437);
		//optionally set collectionID if needed
		//bookmark.setCollectionID((long) NULL);
		bookmark = bookmarksRepository.save(bookmark);

		// Act
		Bookmarks savedBookmark = bookmarksRepository.save(bookmark);

		// Assert
		assertEquals((long) 2, savedBookmark.getAccountID());
		assertTrue(bookmarksRepository.findById(savedBookmark.getId()).isPresent());
	}
	
	@Test
	public void testDeleteById() {
		// Arrange
		Bookmarks bookmark = new Bookmarks();
		bookmark.setAccountID((long) 3);
     	bookmark.setWhichAPI("Delete");
		bookmark.setAPI_identifier("ME");
		bookmark.setTimestamp((long) 13131);
		bookmark.setLatitude(-20);
		bookmark.setLongitude(333);
		//optionally set collectionID if needed
		//bookmark.setCollectionID((long) NULL);
		bookmark = bookmarksRepository.save(bookmark);

		// Act
		bookmarksRepository.deleteById(bookmark.getId());

		// Assert
		assertFalse(bookmarksRepository.findById(bookmark.getId()).isPresent());
	}

}

