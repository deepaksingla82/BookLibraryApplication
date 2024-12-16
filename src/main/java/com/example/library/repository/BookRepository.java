package com.example.library.repository;

import com.example.library.bean.Book;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookRepository {

    public static final List<Book> bookList = new ArrayList<>();

    static {
        Book book1 = new Book("isbn001", "Harry Potter", "JK Rowling", 2000, 10);
        Book book2 = new Book("isbn002", "Wrost Boy", "David Williams", 2012, 50);
        Book book3 = new Book("isbn003", "Play with Kitten", "David Williams", 2004, 20);
        Book book4 = new Book("isbn004", "Life of Pi", "Yann Martel", 2001, 40);
        Book book5 = new Book("isbn005", "Lord of the Ring", "Tolkien", 1955, 30);

        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);
        bookList.add(book4);
        bookList.add(book5);
    }

    public List<Book> getBooks(){
        return bookList;
    }

}