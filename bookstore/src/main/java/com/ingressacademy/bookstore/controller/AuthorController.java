package com.ingressacademy.bookstore.controller;

import com.ingressacademy.bookstore.config.JwtService;
import com.ingressacademy.bookstore.dto.CreateBookDto;
import com.ingressacademy.bookstore.dto.MailDetailsDto;
import com.ingressacademy.bookstore.dto.MessageResponse;
import com.ingressacademy.bookstore.service.AuthorService;
import com.ingressacademy.bookstore.service.BookService;
import com.ingressacademy.bookstore.service.MailService;
import com.ingressacademy.bookstore.model.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/author")
public class AuthorController {
    private final AuthorService authorService;
    private final BookService bookService;
    private final JwtService jwtService;
    private final MailService mailService;

    public AuthorController(AuthorService authorService, BookService bookService, JwtService jwtService, MailService mailService) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.jwtService = jwtService;
        this.mailService = mailService;
    }
    @PreAuthorize("hasAuthority('author:create')")
    @PostMapping("/createBook")
    public ResponseEntity<Book> getAuthorBooks(@RequestBody CreateBookDto createBookDto , @RequestHeader("Authorization") String token) {
        String email = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(bookService.createBook(createBookDto, email));
    }
    @PreAuthorize("hasAuthority('author:read')")
    @GetMapping("/getBooks")
    public ResponseEntity<List<Book>> getAuthorBooks(@RequestHeader("Authorization") String token) {
        String email = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(bookService.getBooksByAuthor(email));
    }
    @PreAuthorize("hasAuthority('author:delete')")
    @DeleteMapping("/deleteBook")
    public ResponseEntity<MessageResponse> deleteBook(@RequestParam Long bookId , @RequestHeader("Authorization") String token) {
        String email = jwtService.extractUsername(token.substring(7));
        bookService.deleteBook(bookId);
        return ResponseEntity.ok(new MessageResponse("Book deleted"));
    }
    @PostMapping("/addEmailDetails")
    public ResponseEntity<MessageResponse> addEmailDetails(@RequestBody MailDetailsDto email, @RequestHeader("Authorization") String token) {
        String authorEmail = jwtService.extractUsername(token.substring(7));
        mailService.addMailToAuthor(authorEmail, email);
        return ResponseEntity.ok(new MessageResponse("Email details added"));
    }


}
