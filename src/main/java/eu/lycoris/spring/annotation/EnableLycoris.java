package eu.lycoris.spring.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import eu.lycoris.spring.configuration.LycorisConfiguration;
import eu.lycoris.spring.property.LycorisProperties;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
@Import({LycorisConfiguration.class})
@EnableConfigurationProperties({LycorisProperties.class})
public @interface EnableLycoris {}
