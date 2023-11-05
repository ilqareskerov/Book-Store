package com.ingressacademy.bookstore.token;

import com.ingressacademy.bookstore.model.Author;
import com.ingressacademy.bookstore.model.Student;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {

  @Id
  @GeneratedValue
  public Integer id;

  @Column(unique = true)
  public String token;

  @Enumerated(EnumType.STRING)
  public TokenType tokenType = TokenType.BEARER;

  public boolean revoked;

  public boolean expired;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinTable(name = "student_token",
          joinColumns = @JoinColumn(name = "token_id"),
          inverseJoinColumns = @JoinColumn(name = "student_id"))
  public Student student;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinTable(name = "author_token",
          joinColumns = @JoinColumn(name = "token_id"),
          inverseJoinColumns = @JoinColumn(name = "author_id"))
  public Author author;
}
