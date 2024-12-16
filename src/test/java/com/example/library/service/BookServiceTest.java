package com.example.library.service;


import com.example.library.bean.Book;
import com.example.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllBooks(){
        List<Book> books = getMockedBooks();
        when(bookRepository.getBooks()).thenReturn(books);

        List<Book> booksReturned = bookService.getAllBooks();

        assertThat(booksReturned).isNotNull();
        assertThat(booksReturned.size()).isEqualTo(3);
        verify(bookRepository, times(1)).getBooks();
    }


    @Test
    public void testIsBookAlreadyExist_WhenBookExists_ShouldReturnTrue(){
        String inputIsbn = "A1";
        when(bookRepository.getBooks()).thenReturn(getMockedBooks());

        boolean isBookExists = bookService.isBookAlreadyExist(inputIsbn);

        assertEquals(true, isBookExists);
    }


    @Test
    public void testIsBookAlreadyExist_WhenBookDoesNotExists_ShouldReturnFalse(){
        String inputIsbn = "ZA1";
        when(bookRepository.getBooks()).thenReturn(getMockedBooks());

        boolean isBookExists = bookService.isBookAlreadyExist(inputIsbn);

        assertEquals(false, isBookExists);
    }


    @Test
    public void testAddBook_withNullBookPassedIn_ShouldReturnNull(){
        Book newBookToAdd = null;

        Book bookAdded = bookService.addBook(newBookToAdd);

        assertNull(bookAdded);
        verify(bookRepository, times(0)).getBooks();
    }


    @Test
    public void testAddBook_withNewBookPassedIn_ShouldReturnNewBook(){
        Book newBookToAdd = new Book("A11", "Twilight", "Stephenie Meyer", 2000, 12);
        when(bookRepository.getBooks()).thenReturn(getMockedBooks());

        Book bookAdded = bookService.addBook(newBookToAdd);

        assertEquals(newBookToAdd.getIsbn(), bookAdded.getIsbn());
        verify(bookRepository, times(1)).getBooks();
    }

    @Test
    public void testRemoveBook(){
        String inputIsbn = "A2";
        List<Book> bookList = getMockedBooks();
        when(bookRepository.getBooks()).thenReturn(bookList);

        bookService.removeBook(inputIsbn);

        assertNotNull(bookList);
        assertEquals(2, bookList.size());
        verify(bookRepository, times(1)).getBooks();
    }

    @Test
    public void testFindBookByIsbn_whenInvalidIsbnPassed_shouldReturnNull(){
        String inputIsbn = "Z22";
        when(bookRepository.getBooks()).thenReturn(getMockedBooks());

        Book bookFound = bookService.findBookByIsbn(inputIsbn);

        assertNull(bookFound);
        verify(bookRepository, times(1)).getBooks();
    }


    @Test
    public void testFindBookByIsbn_whenValidIsbnPassed_shouldReturnBook(){
        String inputIsbn = "A2";
        when(bookRepository.getBooks()).thenReturn(getMockedBooks());

        Book bookFound = bookService.findBookByIsbn(inputIsbn);

        assertNotNull(bookFound);
        assertEquals(inputIsbn, bookFound.getIsbn());
        verify(bookRepository, times(1)).getBooks();
    }


    @Test
    public void testFindBooksByAuthor_whenValidAuthorPassed_shouldReturnBookList(){
        String inputAuthor = "Author 1";
        when(bookRepository.getBooks()).thenReturn(getMockedBooks());

        List<Book> booksList = bookService.findBooksByAuthor(inputAuthor);

        assertNotNull(booksList);
        assertEquals(2, booksList.size());
        verify(bookRepository, times(1)).getBooks();
    }


    @Test
    public void testFindBooksByAuthor_whenInvalidAuthorPassed_shouldReturnBookList(){
        String inputAuthor = "Not Existing";
        when(bookRepository.getBooks()).thenReturn(getMockedBooks());

        List<Book> booksList = bookService.findBooksByAuthor(inputAuthor);

        assertNotNull(booksList);
        assertEquals(0, booksList.size());
        verify(bookRepository, times(1)).getBooks();
    }


    @Test
    public void testBorrowBook_whenInvalidIsbnPassed_shouldReturnNoBook(){
        String inputIsbn = "AA";
        when(bookRepository.getBooks()).thenReturn(getMockedBooks());

        Optional<Book> optionalBook = bookService.borrowBook(inputIsbn);

        assertNotNull(optionalBook);
        assertEquals(true, optionalBook.isEmpty());
        verify(bookRepository, times(1)).getBooks();
    }


    @Test
    public void testBorrowBook_whenValidIsbnPassed_shouldReturnBookWithReducedCopiesAvailable(){
        String inputIsbn = "A2";
        when(bookRepository.getBooks()).thenReturn(getMockedBooks());

        Optional<Book> optionalBook = bookService.borrowBook(inputIsbn);

        assertNotNull(optionalBook);
        assertEquals(true, optionalBook.isPresent());
        assertEquals(inputIsbn, optionalBook.get().getIsbn());
        assertEquals(9, optionalBook.get().getAvailableCopies());
        verify(bookRepository, times(1)).getBooks();
    }


    @Test
    public void testBorrowBook_whenValidIsbnPassed_AndNoCopiesAvailable_shouldReturnNoBook(){
        String inputIsbn = "A22";
        Book aBook = new Book("A22", "My Title", "A Author", 2001, 0);
        List<Book> bookList = new ArrayList<>();
        bookList.add(aBook);
        when(bookRepository.getBooks()).thenReturn(bookList);

        Optional<Book> optionalBook = bookService.borrowBook(inputIsbn);

        assertNotNull(optionalBook);
        assertEquals(true, optionalBook.isEmpty());
        verify(bookRepository, times(1)).getBooks();
    }


    @Test
    public void testReturnBook_whenInvalidIsbnPassed_shouldReturnNoBook(){
        String inputIsbn = "AA";
        when(bookRepository.getBooks()).thenReturn(getMockedBooks());

        Book book = bookService.returnBook(inputIsbn);

        assertNull(book);
        verify(bookRepository, times(1)).getBooks();
    }


    @Test
    public void testReturnBook_whenValidIsbnPassed_shouldReturnBook(){
        String inputIsbn = "A2";
        when(bookRepository.getBooks()).thenReturn(getMockedBooks());

        Book bookToReturn = bookService.returnBook(inputIsbn);

        assertNotNull(bookToReturn);
        assertEquals(11, bookToReturn.getAvailableCopies());
        verify(bookRepository, times(1)).getBooks();
    }


    private static List<Book> getMockedBooks(){
        Book book1 = new Book("A1", "Title 1", "Author 1", 2000, 25);
        Book book2 = new Book("A2", "Title 2", "Author 2", 1990, 10);
        Book book3 = new Book("A3", "Title 3", "Author 1", 2002, 20);

        List<Book> booksList = new ArrayList<>();
        booksList.add(book1);
        booksList.add(book2);
        booksList.add(book3);
        return booksList;
    }
}
