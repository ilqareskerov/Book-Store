package com.ingressacademy.bookstore.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

  @Query("SELECT t FROM Token t inner join Student s on t.student.id = s.id WHERE s.id = :id AND (t.expired = false OR t.revoked = false)")
  List<Token> findAllValidTokenByStudent(Long id);

  @Query("SELECT t FROM Token t inner join Author a on t.author.id = a.id WHERE a.id = :id AND (t.expired = false OR t.revoked = false)")
  List<Token> findAllValidTokenByAuthor(Long id);

  Optional<Token> findByToken(String token);
}
