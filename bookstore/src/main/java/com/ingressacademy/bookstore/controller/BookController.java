package com.ingressacademy.bookstore.controller;

import com.ingressacademy.bookstore.service.BookService;
import com.ingressacademy.bookstore.model.Book;
import com.ingressacademy.bookstore.model.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    @PreAuthorize("hasAuthority('author:read')")
    @GetMapping("/getStudents")
    public ResponseEntity<List<Student>> getStudentsByBooks(String bookName) {
        return ResponseEntity.ok(bookService.getStudentsByBook(bookName));
    }
    @GetMapping("/getBooks")
    public ResponseEntity<List<Book>> getBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

}
