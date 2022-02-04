package eu.lycoris.spring.web;

import eu.lycoris.spring.configuration.LycorisWebConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@AutoConfigureAfter(LycorisWebConfiguration.class)
public class LycorisWebMvcConfigurer implements WebMvcConfigurer {

  @Autowired(required = false)
  private List<HandlerInterceptor> interceptors;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    if (interceptors == null) {
      return;
    }
    interceptors.stream().forEach(registry::addInterceptor);
  }
}
