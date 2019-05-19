package eu.lycoris.spring.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import eu.lycoris.spring.configuration.LycorisSwaggerConfiguration;
import eu.lycoris.spring.property.LycorisProperties;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
@EnableLycoris
@EnableLycorisWeb
@Import({LycorisSwaggerConfiguration.class})
@EnableConfigurationProperties({LycorisProperties.class})
public @interface EnableLycorisSwagger {}
