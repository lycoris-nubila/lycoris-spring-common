package eu.lycoris.spring.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import eu.lycoris.spring.configuration.LycorisGraphQLConfiguration;
import eu.lycoris.spring.configuration.LycorisWebConfiguration;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
@EnableLycoris
@Import({LycorisGraphQLConfiguration.class, LycorisWebConfiguration.class})
public @interface EnableLycorisGraphQL {}
