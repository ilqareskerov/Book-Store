package com.ingressacademy.bookstore.service;

import com.ingressacademy.bookstore.repo.AuthorRepository;
import com.ingressacademy.bookstore.model.Author;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }
    public boolean isAuthorExist(String username){
        return authorRepository.getAuthorByEmail(username).isPresent();
    }
    public Author getAuthorByEmail(String username){
        if (!isAuthorExist(username)){
            throw new RuntimeException("Author not found");
        }
        return authorRepository.getAuthorByEmail(username).get();
    }
    @Transactional
    public Author saveAuthor(Author author){
        return authorRepository.save(author);
    }


}
