package com.ingressacademy.bookstore.service;

import com.ingressacademy.bookstore.dto.BookDto;
import com.ingressacademy.bookstore.dto.CreateBookDto;
import com.ingressacademy.bookstore.dto.Mail;
import com.ingressacademy.bookstore.repo.BookRepository;
import com.ingressacademy.bookstore.model.Author;
import com.ingressacademy.bookstore.model.Book;
import com.ingressacademy.bookstore.model.Student;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final StudentService studentService;
    private final AuthorService authorService;
    private final MailService mailService;

    public BookService(BookRepository bookRepository, StudentService studentService, AuthorService authorService, MailService mailService) {
        this.bookRepository = bookRepository;
        this.studentService = studentService;
        this.authorService = authorService;
        this.mailService = mailService;
    }
    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }
    public Book getBookById(Long id){
        return bookRepository.findById(id).orElseThrow(()->new RuntimeException("Book not found"));
    }
    public Book studentRegisterBook(BookDto book, String username){
        Student student=studentService.getStudentByEmail(username);
        Book book1 = getBookByName(book.getName());
        if (student.getReadingBooks().contains(book1)){
            throw new RuntimeException("You already read this book");
        }
        book1.getStudents().add(student);
        student.getReadingBooks().add(book1);
        studentService.saveStudent(student);
        return bookRepository.save(book1);
    }
    public List<Book> getAllBooksByStudentUsername(String username){
        Student student = studentService.getStudentByEmail(username);
        return student.getReadingBooks();
    }
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }
    public Book getBookByName(String name){
        return bookRepository.getBookByName(name).orElseThrow(()->new RuntimeException("Book not found"));
    }
    public Book saveBook(Book book){
        return bookRepository.save(book);
    }
    public boolean isBookExist(String name){
        return bookRepository.existsByName(name).orElseThrow(()->new RuntimeException("Book not found"));
    }
    @Transactional
    public Book createBook(CreateBookDto book , String authorEmail){
        Book newBook = new Book();
        Author author = authorService.getAuthorByEmail(authorEmail);
        newBook.setName(book.getName());
        newBook.setAuthor(author);
        author.getBooks().add(newBook);
        authorService.saveAuthor(author);
        Mail mail = new Mail();
        mail.setSubject("New book");
        mail.setMessage("New book is added " + newBook.getName() + " by " + author.getFullName());
//        mailService.sendMail(mail , author);
        return bookRepository.save(newBook);
    }
    public List<Book> getBooksByAuthor(String authorEmail){
        Author author = authorService.getAuthorByEmail(authorEmail);
        return author.getBooks();
    }
    public List<Student>getStudentsByBook(String bookName){
        Book book = getBookByName(bookName);
        return book.getStudents();
    }
    public void deleteBook(Long bookId , String authorEmail){
        Book book = getBookById(bookId);
        Author author = authorService.getAuthorByEmail(authorEmail);
        author.getBooks().remove(book);
        authorService.saveAuthor(author);
        deleteBook(bookId);
    }
}
