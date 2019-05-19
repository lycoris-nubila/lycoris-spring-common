package eu.lycoris.spring.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import eu.lycoris.spring.configuration.LycorisWebConfiguration;
import eu.lycoris.spring.web.LycorisControllerAdvice;
import eu.lycoris.spring.web.LycorisWebMvcConfigurer;
import eu.lycoris.spring.web.LycorisWebSecurityConfigurerAdapter;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
@EnableLycoris
@Import({
  LycorisWebConfiguration.class,
  LycorisWebMvcConfigurer.class,
  LycorisWebSecurityConfigurerAdapter.class,
  LycorisControllerAdvice.class
})
public @interface EnableLycorisWeb {}
