package com.jeferro.shared.auth.infrastructure.rest.jwt;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.jeferro.shared.auth.infrastructure.ContextManager;
import com.jeferro.shared.auth.infrastructure.rest.configurations.RestSecurityProperties;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HeaderJwtDecoder {

  private static final Logger logger = LoggerFactory.getLogger(HeaderJwtDecoder.class);

  private static final String BEARER_PREFIX = "Bearer ";

  public static final String ROLES_CLAIM = "roles";

  private final RestSecurityProperties jwtProperties;

  private final Algorithm hmac512;

  private final JWTVerifier jwtVerifier;

  public HeaderJwtDecoder(RestSecurityProperties restSecurityProperties) {
    this.jwtProperties = restSecurityProperties;

    hmac512 = Algorithm.HMAC512(restSecurityProperties.issuer());
    jwtVerifier = JWT.require(hmac512).build();
  }

  public void decode(HttpServletRequest request) {
    try {
      var header = request.getHeader(AUTHORIZATION);

      if (header == null || !belongsToJwt(header)) {
        return;
      }

      var jwtToken = header.substring(BEARER_PREFIX.length());
      var jwt = jwtVerifier.verify(jwtToken);

      var username = jwt.getSubject();
      var roles = new HashSet<>(jwt.getClaim(ROLES_CLAIM).asList(String.class));

      ContextManager.signInFromWeb(request, username, roles);
    } catch (JWTVerificationException cause) {
      logger.error("Error processing request", cause);
    }
  }

  public String encode(String username, Set<String> roles) {
    var issuedAt = Instant.now();

    var jwtBuilder =
        JWT.create()
            .withIssuer(jwtProperties.issuer())
            .withIssuedAt(issuedAt)
            .withSubject(username)
            .withArrayClaim(ROLES_CLAIM, roles.toArray(String[]::new));

    if (jwtProperties.hasDuration()) {
      var expiresAt = issuedAt.plusMillis(jwtProperties.durationAsMillis());

      jwtBuilder.withExpiresAt(expiresAt);
    }

    return BEARER_PREFIX + jwtBuilder.sign(hmac512);
  }

  private boolean belongsToJwt(String header) {
    return header.startsWith(BEARER_PREFIX);
  }
}
