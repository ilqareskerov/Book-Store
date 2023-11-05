package com.ingressacademy.bookstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public class Book extends BaseEntity{
    @Column(unique = true)
    private String name;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;
    @JsonIgnore
    @ManyToMany(mappedBy = "readingBooks")
    private List<Student> students = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
