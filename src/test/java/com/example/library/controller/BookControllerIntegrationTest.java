package com.example.library.controller;

import com.example.library.LibraryApplication;
import com.example.library.bean.Book;
import org.json.JSONException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations="classpath:application-test.properties")
@SpringBootTest(classes = LibraryApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Value("${spring.security.user.name}")
    private String userName;

    @Value("${spring.security.user.password}")
    private String password;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Test
    @Order(1)
    public void testAllBooks() throws JSONException {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.withBasicAuth(userName, password).exchange(
                createURLWithPort("/api/v1/books/"),
                HttpMethod.GET, entity, String.class);

        String expected = "[{\"isbn\":\"isbn001\",\"title\":\"Harry Potter\",\"author\":\"JK Rowling\",\"publicationYear\":2000,\"availableCopies\":10}," +
                "{\"isbn\":\"isbn002\",\"title\":\"Wrost Boy\",\"author\":\"David Williams\",\"publicationYear\":2012,\"availableCopies\":50}," +
                "{\"isbn\":\"isbn003\",\"title\":\"Play with Kitten\",\"author\":\"David Williams\",\"publicationYear\":2004,\"availableCopies\":20}," +
                "{\"isbn\":\"isbn004\",\"title\":\"Life of Pi\",\"author\":\"Yann Martel\",\"publicationYear\":2001,\"availableCopies\":40}," +
                "{\"isbn\":\"isbn005\",\"title\":\"Lord of the Ring\",\"author\":\"Tolkien\",\"publicationYear\":1955,\"availableCopies\":30}]";

        assertEquals(expected, response.getBody(), false);
    }


    @Test
    @Order(2)
    public void testFindBookByIsbn() throws JSONException {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.withBasicAuth(userName, password).exchange(
                createURLWithPort("/api/v1/books/isbn001"),
                HttpMethod.GET, entity, String.class);

        String expected = "{\"isbn\":\"isbn001\",\"title\":\"Harry Potter\",\"author\":\"JK Rowling\",\"publicationYear\":2000,\"availableCopies\":10}";

        assertEquals(expected, response.getBody(), false);
    }

    @Test
    @Order(3)
    public void testFindBookByAuthors() throws JSONException {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.withBasicAuth(userName, password).exchange(
                createURLWithPort("/api/v1/books/author/Yann Martel"),
                HttpMethod.GET, entity, String.class);

        String expected = "[{\"isbn\":\"isbn004\",\"title\":\"Life of Pi\",\"author\":\"Yann Martel\",\"publicationYear\":2001,\"availableCopies\":40}]";

        assertEquals(expected, response.getBody(), false);
    }

    @Test
    @Order(4)
    public void testAddBook() {
        Book newBookToAdd = new Book("isbn200", "Title1", "Author1", 2020, 50);
        HttpEntity<Book> entity = new HttpEntity<>(newBookToAdd, headers);

        ResponseEntity<String> response = restTemplate.withBasicAuth(userName, password).exchange(
                createURLWithPort("/api/v1/books/"),
                HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
        assertEquals("Book with ISBN - " + newBookToAdd.getIsbn() + " added successfully.", response.getBody());
    }

    @Test
    @Order(5)
    public void testRemoveBook(){
        String inputIsbn = "isbn001";
        HttpEntity<String> deleteEntity = new HttpEntity<>(null, headers);
        String deleteUrl = "/api/v1/books/" + inputIsbn;

        ResponseEntity<String> response = restTemplate.withBasicAuth(userName, password).exchange(
                createURLWithPort(deleteUrl),
                HttpMethod.DELETE, deleteEntity, String.class);

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());
    }


    @Test
    @Order(6)
    public void testBorrowBook() {
        String inputIsbn = "isbn004";
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.withBasicAuth(userName, password).exchange(
                createURLWithPort("/api/v1/books/borrow/" + inputIsbn),
                HttpMethod.PUT, entity, (String.class));

        String expected = "{\"isbn\":\"isbn004\",\"title\":\"Life of Pi\",\"author\":\"Yann Martel\",\"publicationYear\":2001,\"availableCopies\":39}";

        assertEquals(expected, response.getBody());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    }


    @Test
    @Order(7)
    public void testReturnBook()  {
        String inputIsbn = "isbn004";
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.withBasicAuth(userName, password).exchange(
                createURLWithPort("/api/v1/books/return/" + inputIsbn),
                HttpMethod.PUT, entity, String.class);

        String expected = "{\"isbn\":\"isbn004\",\"title\":\"Life of Pi\",\"author\":\"Yann Martel\",\"publicationYear\":2001,\"availableCopies\":40}";

        assertEquals(expected, response.getBody());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    }



    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}
