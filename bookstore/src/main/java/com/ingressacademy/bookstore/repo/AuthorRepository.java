package com.ingressacademy.bookstore.repo;
import com.ingressacademy.bookstore.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> getAuthorByEmail(Object email);
    Optional<Author> getAuthorById(Long id);
}
