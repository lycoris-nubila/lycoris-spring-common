package eu.lycoris.spring.common;

import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.NativeWebRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LycorisGraphQLContext {

  private Authentication authentication;

  private NativeWebRequest request;
}
