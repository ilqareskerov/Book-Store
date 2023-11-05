package com.ingressacademy.bookstore.repo;

import com.ingressacademy.bookstore.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsStudentByEmail(String email);
    Optional<Student> findStudentByEmail(String email);
}
