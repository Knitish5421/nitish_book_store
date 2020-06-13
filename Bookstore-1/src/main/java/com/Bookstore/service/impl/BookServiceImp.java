package com.Bookstore.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Bookstore.model.Book;
import com.Bookstore.respository.BookRepository;
@Service
public class BookServiceImp implements BookService
{
	@Autowired
	private BookRepository bookRepository;

	@Override
	public List<Book> findAll()
	{
		List<Book> bookList = (List<Book>) bookRepository.findAll();
		List<Book> activeBookList = new ArrayList<Book>();
		for(Book book : bookList)
		{
			if(book.isActive()) {
				activeBookList.add(book);
			}
		}
		return activeBookList;
		
	}

	@Override
	public Book findById(Long id)
	{
		return  bookRepository.findById(id).get();
	}

	@Override
	public List<Book> findByCategory(String category) {
		
		List<Book> bookList = bookRepository.findByCategory(category);
		List<Book> activeBookList = new ArrayList<Book>();
		for(Book book : bookList)
		{
			if(book.isActive()) {
				activeBookList.add(book);
			}
		}
		return activeBookList;
	}

	@Override
	public List<Book> blurrySearch(String title) {
		List<Book> bookList = bookRepository.findByTitleContaining(title);
		List<Book> activeBookList = new ArrayList<Book>();
		for(Book book : bookList)
		{
			if(book.isActive()) {
				activeBookList.add(book);
			}
		}
		return null;
	}

	

}
