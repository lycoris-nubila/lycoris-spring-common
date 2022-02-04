package eu.lycoris.spring.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.NativeWebRequest;

@Getter
@AllArgsConstructor
public class LycorisGraphQLContext {

  private Authentication authentication;

  private NativeWebRequest request;
}
