package eu.lycoris.spring.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import eu.lycoris.spring.cache.JwtIdKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validator;

@Slf4j
@Aspect
@Configuration
public class LycorisConfiguration {

  @Bean("jwtIdKeyGenerator")
  public KeyGenerator keyGenerator() {
    return new JwtIdKeyGenerator();
  }

  @Bean
  public MethodValidationPostProcessor methodValidationPostProcessor(Validator validator) {
    MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
    postProcessor.setValidator(validator);
    return postProcessor;
  }

  @Bean
  public LocalValidatorFactoryBean validator(MessageSource messageSource) {
    LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    validator.setParameterNameDiscoverer(new LocalVariableTableParameterNameDiscoverer());
    validator.setValidationMessageSource(messageSource);
    return validator;
  }

  @Bean
  public ObjectMapper objectMapper(@Autowired(required = false) FilterProvider filterProvider) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());
    mapper.registerModule(new ParameterNamesModule());
    if (filterProvider != null) {
      mapper.setFilterProvider(filterProvider);
    }
    mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return mapper;
  }

  @Bean
  public JacksonJsonProvider jacksonJsonProvider(ObjectMapper mapper) {
    return new JacksonJsonProvider(mapper);
  }

  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:i18n/messages");
    messageSource.setFallbackToSystemLocale(false);
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setCacheSeconds(3600);
    return messageSource;
  }

  @Around("@annotation(eu.lycoris.spring.common.LycorisLogMethod)")
  public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
    log.debug(
        "Executing method {} with arguments {}",
        joinPoint.getSignature().getName(),
        StringUtils.join(joinPoint.getArgs(), ", "));

    Object result = joinPoint.proceed();

    log.debug(
        "Executed method {} with arguments {} returning {}",
        joinPoint.getSignature().getName(),
        StringUtils.join(joinPoint.getArgs(), ", "),
        result);

    return result;
  }
}
