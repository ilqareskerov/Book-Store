package com.ingressacademy.bookstore.controller;

import com.ingressacademy.bookstore.config.JwtService;
import com.ingressacademy.bookstore.dto.AuthorDto;
import com.ingressacademy.bookstore.dto.BookDto;
import com.ingressacademy.bookstore.service.BookService;
import com.ingressacademy.bookstore.service.StudentService;
import com.ingressacademy.bookstore.model.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( value = "/api/v1/student" ,  produces = "application/json")
public class StudentController {
    private final StudentService studentService;
    private final JwtService jwtService;
    private final BookService bookService;

    public StudentController(StudentService studentService, JwtService jwtService, BookService bookService) {
        this.studentService = studentService;
        this.jwtService = jwtService;
        this.bookService = bookService;
    }

    @GetMapping("/getBooks")
    public ResponseEntity<List<Book>> getStudentBooks(@RequestHeader("Authorization") String token) {
        String email = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(studentService.getStudentBooks(email));
    }

    @PostMapping("/readBook")
    public ResponseEntity<String> readBook(@RequestBody BookDto book, @RequestHeader("Authorization") String token) {
        String email = jwtService.extractUsername(token.substring(7));
        Book book1 = bookService.studentRegisterBook(book, email);
        return ResponseEntity.ok("Book read successfully");
    }

    @PostMapping("/subscribeAuthor")
    public ResponseEntity<String> subscribeAuthor(@RequestBody AuthorDto authorDto, @RequestHeader("Authorization") String token) {
        String email = jwtService.extractUsername(token.substring(7));
        studentService.subscribeAuthor(email, authorDto.getName());
        return ResponseEntity.ok("Author subscribed successfully");

    }
}

