# BookLibraryApplication
A Book Library Application

# Getting Started
Library Application - Spring Boot Gradle Project.

# Project Implementation:
- In memory records to run and test application.
- Simple Cache implementation to improve performance for some endpoints.
- Concurrency Implementation using synchronized keyword on service layer.
- Implemented a basic rate limiting mechanism for the API endpoints.
- Basic Authentication implementation (with username/password defined in application.properties)
- Implementation with Unit Testing along with Integration Tests
- Implementation with some basic (additional) checks - 
  - Checking for existing book when adding a new book
  - When No books for author, it returns empty list.
  - While borrowing a book, it displays error message when book is not available to borrow.
  - Check whether book is available in the system, before we return or borrow the book.


# Steps to Run project:
- Clone the project into local repository.
- Import/Open (File - Open) the gradle project into IDE by selecting local directory for project.
- Build the project using gradle
- then Go to IDE terminal and  execute "./gradlew bootRun" to run Spring boot application.
- Once application has been run successfully, then execute the endpoints on postman or some client application.


### API Reference Documentation:
1. Get All Books:
        Request Type: GET 
        URL: http://localhost:8080/api/v1/books/ 
        Authentication type: Basic Auth: user/password
        Body: -
        Response: JSON

2. Find a Book (By Isbn Number):
        Request Type: GET
        URL: http://localhost:8080/api/v1/books/{isbn}
        Authentication type: Basic Auth: user/password
        Body: -
        Response: JSON

3. Find books by the author
       Request Type: GET
       URL: http://localhost:8080/api/v1/books/author/{author-name}
       Authentication type: Basic Auth: user/password
       Body: -
       Response: JSON

4. Add Book:
       Request Type: POST
       URL: http://localhost:8080/api/v1/books/
       Authentication type: Basic Auth: user/password
       Body: New Book as JSON
       Response: JSON

5. Remove Book:
       Request Type: DELETE
       URL: http://localhost:8080/api/v1/books/{isbn}
       Authentication type: Basic Auth: user/password
       Body: -
       Response: JSON

6. Borrow Book:
      Request Type: PUT
      URL: http://localhost:8080/api/v1/books/borrow/{isbn}
      Authentication type: Basic Auth: user/password
      Body: -
      Response: JSON

7. Return Book:
      Request Type: PUT
      URL: http://localhost:8080/api/v1/books/return/{isbn}
      Authentication type: Basic Auth: user/password
      Body: -
      Response: JSON


### Additional Links




