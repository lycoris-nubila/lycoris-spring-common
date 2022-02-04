package eu.lycoris.spring.jwt;

import eu.lycoris.spring.common.LycorisSkipJwt;
import io.jsonwebtoken.*;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static eu.lycoris.spring.common.LycorisMessages.ERROR_WEB_REQUEST_EXPIRED_JWT;
import static eu.lycoris.spring.common.LycorisMessages.ERROR_WEB_REQUEST_INVALID_JWT;

public class LycorisJwtHandlerInterceptor extends HandlerInterceptorAdapter {

  private MessageSource messageSource;
  private LycorisJwtSecretProvider secretProvider;

  public LycorisJwtHandlerInterceptor(
      LycorisJwtSecretProvider secretProvider, MessageSource messageSource) {
    this.secretProvider = secretProvider;
    this.messageSource = messageSource;
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if (!(handler instanceof HandlerMethod)) {
      return true;
    }

    HandlerMethod handlerMethod = ((HandlerMethod) handler);
    Secured secured = handlerMethod.getMethodAnnotation(Secured.class);
    LycorisSkipJwt skipJwt = handlerMethod.getMethodAnnotation(LycorisSkipJwt.class);

    String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authorization == null || skipJwt != null) {
      SecurityContextHolder.getContext().setAuthentication(null);
      return true;
    }

    String jwtToken = null;
    if (authorization.toLowerCase().startsWith("bearer")) {
      jwtToken = authorization.substring(6).trim();
    } else {
      jwtToken = authorization;
    }

    try {
      Jws<Claims> claims =
          Jwts.parser().setSigningKey(secretProvider.getSecret()).parseClaimsJws(jwtToken);

      String role = claims.getBody().get("role", String.class);
      String[] roles = claims.getBody().get("roles", String[].class);
      if (secured != null && secured.value() != null) {
        List<String> validRoles = Arrays.asList(secured.value());
        List<String> jwtRoles = roles != null ? Arrays.asList(roles) : Collections.emptyList();

        if (jwtRoles.stream().noneMatch(validRoles::contains) && !validRoles.contains(role)) {
          response.sendError(
              HttpServletResponse.SC_UNAUTHORIZED,
              messageSource.getMessage(
                  ERROR_WEB_REQUEST_INVALID_JWT,
                  null,
                  HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                  LocaleContextHolder.getLocale()));
          return false;
        }
      }

      JwtAuthenticationToken authentification =
          new JwtAuthenticationToken(
              new Jwt(
                  jwtToken,
                  claims.getBody().getIssuedAt() != null
                      ? claims.getBody().getIssuedAt().toInstant()
                      : null,
                  claims.getBody().getExpiration() != null
                      ? claims.getBody().getExpiration().toInstant()
                      : null,
                  claims.getHeader(),
                  claims.getBody()));

      SecurityContextHolder.getContext().setAuthentication(authentification);
      return true;
    } catch (ExpiredJwtException exception) {
      response.sendError(
          HttpStatus.UNAUTHORIZED.value(),
          messageSource.getMessage(
              ERROR_WEB_REQUEST_EXPIRED_JWT,
              null,
              HttpStatus.UNAUTHORIZED.getReasonPhrase(),
              LocaleContextHolder.getLocale()));
      return false;
    } catch (JwtException exception) {
      response.sendError(
          HttpServletResponse.SC_UNAUTHORIZED,
          messageSource.getMessage(
              ERROR_WEB_REQUEST_INVALID_JWT,
              null,
              HttpStatus.UNAUTHORIZED.getReasonPhrase(),
              LocaleContextHolder.getLocale()));
      return false;
    }
  }
}
