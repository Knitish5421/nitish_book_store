package com.Bookstore.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Bookstore.model.Book;
import com.Bookstore.model.User;
import com.Bookstore.service.impl.BookService;
import com.Bookstore.service.impl.UserService;

@Controller
public class SearchController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BookService bookService;
	
	@RequestMapping("/searchByCategory")
	public String searchByCategory( @RequestParam("category") String category,
			Model model, Principal principal)
	{
		if(principal!=null) 
		{
			String email = principal.getName();
			User user = userService.findByEmail(email);
			model.addAttribute("user", user);
		}
		String classActiveCategory = "active" + category;
		classActiveCategory = classActiveCategory.replaceAll("\\s+", "");
		classActiveCategory = classActiveCategory.replaceAll("&", "");
		model.addAttribute(classActiveCategory, true);
		
		List<Book> bookList = bookService.findByCategory(category);
		if(bookList.isEmpty())
		{
			model.addAttribute("emptyList", true);
			return "bookshelf";
		}
		return "bookshelf";
		
	}
	
	@RequestMapping("/searchBook")
	public String searchBook( @ModelAttribute("keyword") String keyword,
			Model model, Principal principal)
	{
		if(principal!=null) 
		{
			String email = principal.getName();
			User user = userService.findByEmail(email);
			model.addAttribute("user", user);
		}
		
		List<Book> bookList = bookService.blurrySearch(keyword);
		if(bookList.isEmpty())
		{
			model.addAttribute("emptyList", true);
			return "bookshelf";
		}
		
		model.addAttribute("bookList", bookList);
		
		return "bookshelf";
	}
	

}