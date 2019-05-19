package eu.lycoris.spring.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import eu.lycoris.spring.configuration.LycorisDddConfiguration;

@Retention(RUNTIME)
@Target(TYPE)
@Documented
@EnableLycoris
@EnableLycorisAsync
@Import({LycorisDddConfiguration.class})
public @interface EnableLycorisDdd {}
