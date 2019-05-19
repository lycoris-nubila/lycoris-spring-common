package eu.lycoris.spring.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import eu.lycoris.spring.async.LycorisAsyncConfigurer;
import eu.lycoris.spring.async.LycorisScheduleConfigurer;
import eu.lycoris.spring.configuration.LycorisAsyncConfiguration;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
@EnableLycoris
@Import({
  LycorisAsyncConfiguration.class,
  LycorisAsyncConfigurer.class,
  LycorisScheduleConfigurer.class
})
public @interface EnableLycorisAsync {}
