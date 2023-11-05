package com.ingressacademy.bookstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ingressacademy.bookstore.token.Token;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "students")
public class Student extends BaseEntity implements UserDetails {
    private String fullName;
    private String email;
    private String password;
    private Role role;
    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "student_reading_book",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> readingBooks = new ArrayList<>();
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "student_subscriptions",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> subscribedAuthors;
    @JsonIgnore
    @OneToMany(mappedBy = "student")
    private List<Token> tokens;


    public Student() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Book> getReadingBooks() {
        return readingBooks;
    }

    public void setReadingBooks(List<Book> readingBooks) {
        this.readingBooks = readingBooks;
    }

    public List<Author> getSubscribedAuthors() {
        return subscribedAuthors;
    }

    public void setSubscribedAuthors(List<Author> subscribedAuthors) {
        this.subscribedAuthors = subscribedAuthors;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }
}
