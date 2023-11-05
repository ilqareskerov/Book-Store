package com.ingressacademy.bookstore.service;

import com.ingressacademy.bookstore.repo.StudentRepository;
import com.ingressacademy.bookstore.model.Author;
import com.ingressacademy.bookstore.model.Book;
import com.ingressacademy.bookstore.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final AuthorService authorService;

    public StudentService(StudentRepository studentRepository, AuthorService authorService) {
        this.studentRepository = studentRepository;
        this.authorService = authorService;
    }
    public boolean isStudentExist(String username){
        return studentRepository.findStudentByEmail(username).isPresent();
    }
    public Student getStudentByEmail(String username){
        if (!isStudentExist(username)){
            throw new RuntimeException("Student not found");
        }
        return studentRepository.findStudentByEmail(username).get();
    }
    public Student saveStudent(Student student){
        return studentRepository.save(student);
    }
    public List<Book> getStudentBooks(String username){
        Student student = getStudentByEmail(username);
        return student.getReadingBooks();
    }
    public void subscribeAuthor(String token , String author){
        Student student = getStudentByEmail(token);
       Author author1 = authorService.getAuthorByEmail(author);
        student.getSubscribedAuthors().add(author1);
        authorService.saveAuthor(author1);
        saveStudent(student);
    }
}
