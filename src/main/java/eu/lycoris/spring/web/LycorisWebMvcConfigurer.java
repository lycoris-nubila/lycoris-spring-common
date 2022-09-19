package eu.lycoris.spring.web;

import eu.lycoris.spring.configuration.LycorisWebConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;

@Configuration
@AutoConfigureAfter(LycorisWebConfiguration.class)
public class LycorisWebMvcConfigurer implements WebMvcConfigurer {

  private final @Nullable List<HandlerInterceptor> interceptors;

  public LycorisWebMvcConfigurer(@Nullable List<HandlerInterceptor> interceptors) {
    this.interceptors = interceptors;
  }

  @Override
  public void addInterceptors(@NotNull InterceptorRegistry registry) {
    if (this.interceptors == null) {
      return;
    }
    this.interceptors.forEach(registry::addInterceptor);
  }
}
