package com.ingressacademy.bookstore.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingressacademy.bookstore.repo.AuthorRepository;
import com.ingressacademy.bookstore.repo.StudentRepository;
import com.ingressacademy.bookstore.token.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final AuthorUserDetailsService authorUserDetailsService;
  private final StudentUserDetailsService studentUserDetailsService;
  private final StudentRepository studentRepository;
    private final AuthorRepository authorRepository;
  private final TokenRepository tokenRepository;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    try {
//      if (request.getServletPath().contains("/api/v1/auth")) {
//        filterChain.doFilter(request, response);
//        return;
//      }
      final String authHeader = request.getHeader("Authorization");
      final String jwt;
      final String userEmail;
      if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
        filterChain.doFilter(request, response);
        return;
      }
      jwt = authHeader.substring(7);
      userEmail = jwtService.extractUsername(jwt);
      if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails ;
        Boolean isTokenValid;
        if(studentRepository.findStudentByEmail(userEmail).isPresent()){
          userDetails = this.studentUserDetailsService.loadUserByUsername(userEmail);
          isTokenValid = tokenRepository.findByToken(jwt)
                  .map(t -> !t.isExpired() && !t.isRevoked())
                  .orElse(false);
        }else {
          userDetails = this.authorUserDetailsService.loadUserByUsername(userEmail);
          isTokenValid = tokenRepository.findByToken(jwt)
                  .map(t -> !t.isExpired() && !t.isRevoked())
                  .orElse(false);
        }
        if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                  userDetails,
                  null,
                  userDetails.getAuthorities()
          );
          authToken.setDetails(
                  new WebAuthenticationDetailsSource().buildDetails(request)
          );
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
      filterChain.doFilter(request, response);
    }catch (Exception e) {
      sendError(response, e);
    }
    }
  private void sendError(HttpServletResponse res, Exception e) throws IOException {
    res.setContentType("application/json");
    Map<String, String> errors = new HashMap<>();
    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    errors.put("error", e.getMessage());
    ObjectMapper mapper = new ObjectMapper();
    res.getWriter().write(mapper.writeValueAsString(errors));
  }
}
