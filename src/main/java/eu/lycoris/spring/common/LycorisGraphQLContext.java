package eu.lycoris.spring.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class LycorisGraphQLContext {

  private @NotNull Authentication authentication;

  private @NotNull NativeWebRequest request;
}
