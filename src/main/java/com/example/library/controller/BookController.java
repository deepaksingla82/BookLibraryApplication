package com.example.library.controller;

import com.example.library.bean.Book;
import com.example.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/books/")
public class BookController {

    private static final String BOOK_ALREADY_EXIST_IN_SYSTEM = "Book already exists in the system";

    private static final String BOOK_NOT_EXIST_IN_SYSTEM = "Book not exists in the system";

    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public ResponseEntity<List<Book>> getAllBooks(){
        List<Book> allBooks = bookService.getAllBooks();
        if(CollectionUtils.isEmpty(allBooks)){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allBooks, HttpStatus.OK);
    }


    @PostMapping( value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addBook(@Validated @RequestBody Book newBook){
        if (bookService.isBookAlreadyExist(newBook.getIsbn())) {
            return new ResponseEntity<>(BOOK_ALREADY_EXIST_IN_SYSTEM, HttpStatus.BAD_REQUEST);
        }
        bookService.addBook(newBook);
        return new ResponseEntity<>("Book with ISBN - " + newBook.getIsbn() + " added successfully.", HttpStatus.CREATED);
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<String> removeBook(@Validated @PathVariable("isbn") String isbn){
        if (!bookService.isBookAlreadyExist(isbn)) {
            return new ResponseEntity<>(BOOK_NOT_EXIST_IN_SYSTEM, HttpStatus.BAD_REQUEST);
        }
        bookService.removeBook(isbn);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{isbn}")
    public ResponseEntity findBookByISBN(@PathVariable("isbn") String isbn){
        Book bookReturned = bookService.findBookByIsbn(isbn);
        if(bookReturned == null){
            return new ResponseEntity(BOOK_NOT_EXIST_IN_SYSTEM, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity(bookReturned, HttpStatus.OK);
        }
     }

    @GetMapping("author/{author}")
    public ResponseEntity findBooksByAuthor(@PathVariable("author") String author){
        List<Book> booksReturned = bookService.findBooksByAuthor(author);
        if(booksReturned == null || booksReturned.size() <= 0){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(Optional.of(booksReturned), HttpStatus.OK);
        }
    }

    @PutMapping("/borrow/{isbn}")
    public ResponseEntity borrowBook(@PathVariable("isbn") String isbn){
        if (!bookService.isBookAlreadyExist(isbn)) {
            return new ResponseEntity<>(BOOK_NOT_EXIST_IN_SYSTEM, HttpStatus.BAD_REQUEST);
        }
        Optional<Book> bookBorrowed = bookService.borrowBook(isbn);
        if (bookBorrowed.isEmpty()) {
            return new ResponseEntity("Book not available for borrowing", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(Optional.of(bookBorrowed), HttpStatus.OK);
        }
    }


    @PutMapping("/return/{isbn}")
    public ResponseEntity returnBook(@PathVariable("isbn") String isbn){
        if (!bookService.isBookAlreadyExist(isbn)) {
            return new ResponseEntity<>(BOOK_NOT_EXIST_IN_SYSTEM, HttpStatus.BAD_REQUEST);
        }
        Book returnBook = bookService.returnBook(isbn);
        return new ResponseEntity(returnBook, HttpStatus.OK);
    }


}