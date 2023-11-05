package com.ingressacademy.bookstore.config;

import com.ingressacademy.bookstore.repo.StudentRepository;
import com.ingressacademy.bookstore.model.Student;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class StudentUserDetailsService implements UserDetailsService {
    private final StudentRepository studentRepository;

    public StudentUserDetailsService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepository.findStudentByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var roles = student.getAuthorities();
        return new org.springframework.security.core.userdetails.User(student.getUsername(), student.getPassword(), roles);
    }
}
