package com.ingressacademy.bookstore.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingressacademy.bookstore.config.JwtService;
import com.ingressacademy.bookstore.repo.AuthorRepository;
import com.ingressacademy.bookstore.repo.StudentRepository;
import com.ingressacademy.bookstore.token.Token;
import com.ingressacademy.bookstore.token.TokenRepository;
import com.ingressacademy.bookstore.token.TokenType;
import com.ingressacademy.bookstore.model.Author;
import com.ingressacademy.bookstore.model.Role;
import com.ingressacademy.bookstore.model.Student;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final StudentRepository studentRepository;
  private final AuthorRepository authorRepository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse registerStudent(RegisterRequest request) {
    if(authorRepository.getAuthorByEmail(request.getEmail()).isPresent() || studentRepository.findStudentByEmail(request.getEmail()).isPresent()){
      throw new RuntimeException("Email already exists");
    }
    Student user = new Student();
    user.setFullName(request.getFullName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(Role.STUDENT);

    var savedUser = studentRepository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserTokenStudent(savedUser, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }
  public AuthenticationResponse registerAuthor(RegisterRequest request) {
    if(studentRepository.findStudentByEmail(request.getEmail()).isPresent() || authorRepository.getAuthorByEmail(request.getEmail()).isPresent()){
      throw new RuntimeException("Email already exists");
    }
    Author user = new Author();
    user.setFullName(request.getFullName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(Role.AUTHOR);
    var savedUser = authorRepository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserTokenAuthor(savedUser, jwtToken);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }
  public AuthenticationResponse authenticateAuthor(AuthenticationRequest request) {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );
    var user = authorRepository.getAuthorByEmail(request.getEmail())
            .orElseThrow();
    user.getAuthorities().stream().map(x -> x.getAuthority()).forEach(System.out::println);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokensAuthor(user);
    saveUserTokenAuthor(user, jwtToken);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }
  public AuthenticationResponse authenticateStudent(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = studentRepository.findStudentByEmail(request.getEmail())
        .orElseThrow();
//    user.getAuthorities().stream().map(x -> x.getAuthority()).forEach(System.out::println);
//    System.out.println(Permission.STUDENT_READ.getPermission());
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokensStudent(user);
    saveUserTokenStudent(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  private void saveUserTokenAuthor(Author user, String jwtToken) {
    var token = Token.builder()
        .author(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }
  private void saveUserTokenStudent(Student user, String jwtToken) {
    var token = Token.builder()
            .student(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
    tokenRepository.save(token);
  }
  private void revokeAllUserTokensStudent(Student user) {
    var validUserTokens = tokenRepository.findAllValidTokenByStudent(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }
  private void revokeAllUserTokensAuthor(Author user) {
    var validUserTokens = tokenRepository.findAllValidTokenByAuthor(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshTokenForStudent(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.studentRepository.findStudentByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokensStudent(user);
        saveUserTokenStudent(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
  public void refreshTokenForAuthor(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.authorRepository.getAuthorByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokensAuthor(user);
        saveUserTokenAuthor(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
