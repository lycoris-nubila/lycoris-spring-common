package eu.lycoris.spring.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan({"eu.lycoris.spring.ddd"})
@ComponentScan("eu.lycoris.spring.ddd")
@EnableJpaRepositories({"eu.lycoris.spring.ddd"})
public class LycorisDddConfiguration {}
