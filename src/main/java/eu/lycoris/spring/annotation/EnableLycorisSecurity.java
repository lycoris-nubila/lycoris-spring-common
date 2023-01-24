package eu.lycoris.spring.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import eu.lycoris.spring.configuration.LycorisSecurityConfiguration;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Retention(RUNTIME)
@Target(TYPE)
@Documented
@EnableLycoris
@Import({LycorisSecurityConfiguration.class})
public @interface EnableLycorisSecurity {}
