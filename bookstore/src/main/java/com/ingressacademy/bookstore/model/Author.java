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
@Table(name = "authors")
public class Author extends BaseEntity implements UserDetails {
    private String fullName;
    private String email;
    private String password;
    private Role role;
    @JsonIgnore
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "author")
    private List<Token> tokens;

    public Author() {
    }
    @ManyToMany(mappedBy = "subscribedAuthors")
    private List<Student> subscribedStudents;
    @OneToOne
    private EmailDetails emailDetails;

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

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Student> getSubscribedStudents() {
        return subscribedStudents;
    }

    public void setSubscribedStudents(List<Student> subscribedStudents) {
        this.subscribedStudents = subscribedStudents;
    }

    public EmailDetails getEmailDetails() {
        return emailDetails;
    }

    public void setEmailDetails(EmailDetails emailDetails) {
        this.emailDetails = emailDetails;
    }
}
