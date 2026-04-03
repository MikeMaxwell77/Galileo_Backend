package edu.uscb.csci570sp26.galileo_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uscb.csci570sp26.galileo_backend.model.Bookmarks;

public interface BookmarksRepository extends JpaRepository<Bookmarks, Long> {

}
