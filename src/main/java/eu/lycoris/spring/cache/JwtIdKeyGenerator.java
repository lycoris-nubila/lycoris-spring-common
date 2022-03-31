package eu.lycoris.spring.cache;

import eu.lycoris.spring.common.LycorisApplicationException;
import eu.lycoris.spring.common.LycorisGraphQLContext;
import lombok.NonNull;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.lang.reflect.Method;
import java.util.stream.Stream;

public class JwtIdKeyGenerator implements KeyGenerator {
  @Override
  public @NonNull Object generate(
      @NonNull Object target, @NonNull Method method, Object @NonNull ... params) {
    LycorisGraphQLContext context =
        (LycorisGraphQLContext)
            Stream.of(params)
                .filter(param -> param instanceof LycorisGraphQLContext)
                .findFirst()
                .orElseThrow(
                    () ->
                        new LycorisApplicationException(
                            "No LycorisGraphQLContext found in params"));

    Jwt jwt = ((JwtAuthenticationToken) context.getAuthentication()).getToken();

    return jwt.getClaimAsString("id");
  }
}
