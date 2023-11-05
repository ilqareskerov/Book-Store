package com.ingressacademy.bookstore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class EmailDetails extends BaseEntity{
    private String username;
    private String password;
    @OneToOne(mappedBy = "emailDetails")
    @JoinTable(
            name = "email_details_author",
            joinColumns = @JoinColumn(name = "email_details_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Author author;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
