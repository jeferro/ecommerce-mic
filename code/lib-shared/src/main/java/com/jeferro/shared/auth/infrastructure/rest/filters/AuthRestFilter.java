package com.jeferro.shared.auth.infrastructure.rest.filters;

import com.jeferro.shared.auth.infrastructure.rest.jwt.HeaderJwtDecoder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class AuthRestFilter extends OncePerRequestFilter {

  private final HeaderJwtDecoder headerJwtDecoder;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    authenticateUser(request);

    filterChain.doFilter(request, response);
  }

  private void authenticateUser(HttpServletRequest request) {
    headerJwtDecoder.decode(request);
  }
}
