package com.example.library.controller;


import com.example.library.bean.Book;
import com.example.library.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @InjectMocks
    private BookController controller;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetAllBooks_GivenBooks_shouldReturn200() throws Exception{
        given(bookService.getAllBooks()).willReturn(getMockedBooks());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/books/")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetAllBooks_GivenNoBooks_shouldReturn204() throws Exception{
        given(bookService.getAllBooks()).willReturn(null);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/books/")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    @Test
    public void testAddBook_GivenNewBook_shouldReturn201() throws Exception{
        Book newBook = new Book("A11", "Title 11", "Author 11", 2011, 11);
        given(bookService.isBookAlreadyExist(newBook.getIsbn())).willReturn(false);
        given(bookService.addBook(newBook)).willReturn(newBook); // some problem here, TODO to fix.

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/books/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBook))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testAddBook_GivenExistingBook_shouldReturn400() throws Exception {
        Book newBook = new Book("A1", "Title 1", "Author 1", 2011, 11);
        given(bookService.isBookAlreadyExist(newBook.getIsbn())).willReturn(true);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/books/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBook))
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testFindBookByIsbn_ValidIsbn_ShouldReturn200() throws Exception {
        String inputIsbn = "A1";
        String url= "/api/v1/books/{isbn}";
        Book aBook = new Book("A1", "Title 1", "Author 1", 2000, 25);
        when(bookService.findBookByIsbn(stringArgumentCaptor.capture())).thenReturn(aBook);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                        .get(url, inputIsbn)
                        .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
        verify(bookService,times(1)).findBookByIsbn(anyString());
        assertEquals(inputIsbn, stringArgumentCaptor.getValue());
    }

    @Test
    public void testFindBookByIsbn_InValidIsbn_ShouldReturn400() throws Exception {
        String inputIsbn = "B1";
        String url= "/api/v1/books/{isbn}";
        when(bookService.findBookByIsbn(stringArgumentCaptor.capture())).thenReturn(null);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get(url, inputIsbn)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
        verify(bookService,times(1)).findBookByIsbn(anyString());
        assertEquals(inputIsbn, stringArgumentCaptor.getValue());
    }


    @Test
    public void findBooksByAuthor_givenAuthorBooks_ShouldReturn200() throws Exception {
        String authorName = "Author 1";
        String url= "/api/v1/books/author/{author}";
        Book book1 = new Book("A1", "Title 1", "Author 1", 2000, 25);
        Book book2 = new Book("A3", "Title 3", "Author 1", 2022, 50);
        List booksByAuthor = new ArrayList();
        booksByAuthor.add(book1);
        booksByAuthor.add(book2);

        when(bookService.findBooksByAuthor(stringArgumentCaptor.capture())).thenReturn(booksByAuthor);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get(url, authorName)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
        verify(bookService,times(1)).findBooksByAuthor(anyString());
        assertEquals(authorName, stringArgumentCaptor.getValue());
    }


    @Test
    public void findBooksByAuthor_givenNoBooks_ShouldReturn404() throws Exception {
        String authorName = "Author 1";
        String url= "/api/v1/books/author/{author}";

        when(bookService.findBooksByAuthor(stringArgumentCaptor.capture())).thenReturn(new ArrayList());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get(url, authorName)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
        verify(bookService,times(1)).findBooksByAuthor(anyString());
        assertEquals(authorName, stringArgumentCaptor.getValue());
    }

    private List<Book> getMockedBooks(){
        Book book1 = new Book("A1", "Title 1", "Author 1", 2000, 25);
        Book book2 = new Book("A2", "Title 2", "Author 2", 1990, 2);
        Book book3 = new Book("A3", "Title 3", "Author 1", 2002, 10);

        List<Book> booksList = new ArrayList<>();
        booksList.add(book1);
        booksList.add(book2);
        booksList.add(book3);
        return booksList;
    }

}