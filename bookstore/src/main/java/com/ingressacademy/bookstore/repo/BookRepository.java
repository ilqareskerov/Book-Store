package com.ingressacademy.bookstore.repo;

import com.ingressacademy.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> getBookByName(String name);
    Optional<Boolean> existsByName(String name);

}

