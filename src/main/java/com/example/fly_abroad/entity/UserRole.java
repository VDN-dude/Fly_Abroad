package com.example.fly_abroad.entity;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author Simon Pirko on 28.06.23
 */
public enum UserRole implements GrantedAuthority {

  USER, ADMIN, AIRLINE_ADMIN;

  @Override
  public String getAuthority() {
    return this.name();
  }
}
