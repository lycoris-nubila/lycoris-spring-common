package eu.lycoris.spring.annotation;

import eu.lycoris.spring.configuration.LycorisSqsConfiguration;
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
@Import({LycorisSqsConfiguration.class})
public @interface EnableLycorisSqs {}
