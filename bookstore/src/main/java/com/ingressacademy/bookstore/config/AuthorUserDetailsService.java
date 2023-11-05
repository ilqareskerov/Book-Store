package com.ingressacademy.bookstore.config;

import com.ingressacademy.bookstore.repo.AuthorRepository;
import com.ingressacademy.bookstore.model.Author;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class AuthorUserDetailsService implements UserDetailsService {
    private final AuthorRepository authorRepository;

    public AuthorUserDetailsService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Author author = authorRepository.getAuthorByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

         var roles = author.getAuthorities();
        return new org.springframework.security.core.userdetails.User(author.getUsername(), author.getPassword(), roles);
    }
}
