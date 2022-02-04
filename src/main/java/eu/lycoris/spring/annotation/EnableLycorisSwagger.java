package eu.lycoris.spring.annotation;

import eu.lycoris.spring.configuration.LycorisSwaggerConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
@EnableLycoris
@EnableLycorisWeb
@Import({LycorisSwaggerConfiguration.class})
public @interface EnableLycorisSwagger {}
