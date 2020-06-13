package com.Bookstore.service.impl;


import java.util.List;

import com.Bookstore.model.Book;

public interface BookService
{
   List<Book> findAll();
   Book findById(Long id);
   List<Book> findByCategory(String category);
   
   List<Book> blurrySearch(String title);
}
