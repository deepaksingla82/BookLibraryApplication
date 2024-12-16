package com.example.library.repository;

import com.example.library.bean.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BookRepositoryTest {

    private BookRepository bookRepository;

    @BeforeEach
    public void setup(){
        bookRepository = new BookRepository();
    }

    @Test
    public void testGetAllBooks(){
        List<Book> booksAvailable = bookRepository.getBooks();
        assertEquals(5, booksAvailable.size());
    }

}
