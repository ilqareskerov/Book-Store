package com.ingressacademy.bookstore.config;

import com.ingressacademy.bookstore.repo.AuthorRepository;
import com.ingressacademy.bookstore.repo.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class UsersAuthenticationProvider implements AuthenticationProvider {

    private final AuthorUserDetailsService  userDetailsService;
    private final StudentUserDetailsService studentUserDetailsService;
    private final AuthorRepository authorRepository;
    private final StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsersAuthenticationProvider(AuthorUserDetailsService userDetailsService, StudentUserDetailsService studentUserDetailsService, AuthorRepository authorRepository, StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.studentUserDetailsService = studentUserDetailsService;
        this.authorRepository = authorRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        if(studentRepository.findStudentByEmail(username).isPresent()){
            UserDetails userDetails = studentUserDetailsService.loadUserByUsername(username);
            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
            } else {
                throw new BadCredentialsException("Invalid username or password");
            }
        }
        if (authorRepository.getAuthorByEmail(username).isPresent()){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
            } else {
                throw new BadCredentialsException("Invalid username or password");
            }
        }
        else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
