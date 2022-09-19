package eu.lycoris.spring.configuration;

import com.fasterxml.classmate.TypeResolver;
import eu.lycoris.spring.common.LycorisErrorDto;
import eu.lycoris.spring.property.LycorisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static eu.lycoris.spring.common.LycorisMessages.*;

@Configuration
@EnableSwagger2
public class LycorisSwaggerConfiguration {

  private final @NotNull MessageSource messageSource;

  private final @NotNull TypeResolver typeResolver;

  public LycorisSwaggerConfiguration(@NotNull MessageSource messageSource) {
    this.typeResolver = new TypeResolver();
    this.messageSource = messageSource;
  }

  @Bean
  public @NotNull Docket api(
      @Autowired LycorisProperties properties,
      @Autowired(required = false) @Nullable List<SecurityScheme> securitySchemes) {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(
            properties.getSwagger().getBasePackage() != null
                ? RequestHandlerSelectors.basePackage(properties.getSwagger().getBasePackage())
                : RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build()
        .securitySchemes(securitySchemes)
        .useDefaultResponseMessages(false)
        .additionalModels(this.typeResolver.resolve(LycorisErrorDto.class))
        .globalResponseMessage(RequestMethod.GET, globalResponseMessageForRead())
        .globalResponseMessage(RequestMethod.POST, globalResponseMessageForWrite())
        .globalResponseMessage(RequestMethod.PATCH, globalResponseMessageForWrite())
        .globalResponseMessage(RequestMethod.DELETE, globalResponseMessageForWrite());
  }

  private List<ResponseMessage> globalResponseMessageForRead() {
    return new ArrayList<>(
        Arrays.asList(
            new ResponseMessageBuilder()
                .code(500)
                .responseModel(new ModelRef(LycorisErrorDto.class.getSimpleName()))
                .message(
                    this.messageSource.getMessage(
                        ERROR_WEB_REQUEST_INTERNAL_ERROR,
                        null,
                        "Unexpected internal server error",
                        LocaleContextHolder.getLocale()))
                .build(),
            new ResponseMessageBuilder()
                .code(401)
                .responseModel(new ModelRef(LycorisErrorDto.class.getSimpleName()))
                .message(
                    this.messageSource.getMessage(
                        ERROR_WEB_REQUEST_AUTHENTICATION_ERROR,
                        null,
                        "Invalid access token or insufficient right",
                        LocaleContextHolder.getLocale()))
                .build()));
  }

  private List<ResponseMessage> globalResponseMessageForWrite() {
    List<ResponseMessage> responseMessages = globalResponseMessageForRead();
    responseMessages.add(
        new ResponseMessageBuilder()
            .code(400)
            .responseModel(new ModelRef(LycorisErrorDto.class.getSimpleName()))
            .message(
                this.messageSource.getMessage(
                    ERROR_WEB_REQUEST_INVALID_BODY,
                    null,
                    "Invalid request body",
                    LocaleContextHolder.getLocale()))
            .build());
    return responseMessages;
  }
}
