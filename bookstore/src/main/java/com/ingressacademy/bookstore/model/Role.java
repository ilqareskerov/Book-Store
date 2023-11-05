package com.ingressacademy.bookstore.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;



@RequiredArgsConstructor
public enum Role {
  AUTHOR(
          Set.of(
                  Permission.AUTHOR_READ,
                  Permission.AUTHOR_UPDATE,
                  Permission.AUTHOR_DELETE,
                  Permission.AUTHOR_CREATE
          )
  ),
  STUDENT(
          Set.of(
                  Permission.STUDENT_READ
          )
  )

  ;

  @Getter
  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}
