package eu.lycoris.spring.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import eu.lycoris.spring.configuration.LycorisWebConfiguration;

@Configuration
@ConditionalOnBean({HandlerInterceptor.class})
@AutoConfigureAfter(LycorisWebConfiguration.class)
public class LycorisWebMvcConfigurer implements WebMvcConfigurer {

  @Autowired private List<HandlerInterceptor> interceptors;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    interceptors.stream().forEach(registry::addInterceptor);
  }
}
