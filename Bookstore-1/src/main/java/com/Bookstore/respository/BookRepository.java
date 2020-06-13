package com.Bookstore.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Bookstore.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>
{
	List<Book> findByCategory(String category);
	List<Book> findByTitleContaining(String title);
}
