package com.example.library.service;

import com.example.library.bean.Book;
import com.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Cacheable("books")
    public List<Book> getAllBooks(){
        return bookRepository.getBooks();
    }

    public boolean isBookAlreadyExist(String isbn){
        List<Book> books = bookRepository.getBooks();
        if(books == null){
            return false;
        }
        if(books.stream().anyMatch(book -> book.getIsbn().equalsIgnoreCase(isbn))){
            return true;
        }
        return false;
    }

    public synchronized Book addBook(Book book){
        if(book == null){
            return null;
        }
        List<Book> booksList = bookRepository.getBooks();
        if(booksList == null){
            booksList = new ArrayList<>();
        }
        booksList.add(book);
        return book;
    }


    public synchronized void removeBook(String isbn){
        List<Book> bookList = bookRepository.getBooks();
        if(bookList == null){
            return;
        }
        for(Book book : bookList){
            if(book.getIsbn().equalsIgnoreCase(isbn)){
                bookList.remove(book);
                break;
            }
        }
    }

    public Book findBookByIsbn(String isbn){
        List<Book> books = getAllBooks();
        Book aBook = books.stream().filter(book -> book.getIsbn().equalsIgnoreCase(isbn)).findFirst().orElse(null);
        return aBook;
    }


    public List<Book> findBooksByAuthor(String author){
        List<Book> allBooks = getAllBooks();
        List<Book> authorBooksList = allBooks.stream().filter(book -> book.getAuthor().equalsIgnoreCase(author)).collect(Collectors.toList());
        return authorBooksList;
    }

    public synchronized Optional<Book> borrowBook(String isbn){
        List<Book> allBooks = getAllBooks();
        Optional<Book> bookToBorrowOptional = allBooks.stream().filter(book -> book.getIsbn().equalsIgnoreCase(isbn)).findAny();

        if(bookToBorrowOptional.isEmpty()){
            return Optional.empty();
        }
        Book bookToBorrow = bookToBorrowOptional.get();
        int availableCopies = bookToBorrow.getAvailableCopies();
        if(availableCopies > 0) {
            bookToBorrow.setAvailableCopies(--availableCopies);
            return Optional.of(bookToBorrow);
        } else {
            return Optional.empty();
        }
    }


    public synchronized Book returnBook(String isbn){
        List<Book> allBooks = getAllBooks();
        Book bookToReturn = allBooks.stream().filter(book -> book.getIsbn().equalsIgnoreCase(isbn)).findAny().orElse(null);
        if(bookToReturn == null) {
            return null;
        }
        int availableCopies = bookToReturn.getAvailableCopies();
        bookToReturn.setAvailableCopies(++availableCopies);
        return bookToReturn;
    }

}