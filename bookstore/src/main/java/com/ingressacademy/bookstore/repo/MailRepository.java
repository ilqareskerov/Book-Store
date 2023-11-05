package com.ingressacademy.bookstore.repo;

import com.ingressacademy.bookstore.model.EmailDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MailRepository extends JpaRepository<EmailDetails, Long> {
    @Query("select e from EmailDetails e ")
    Optional<EmailDetails> findByAuthor(String username);

}
