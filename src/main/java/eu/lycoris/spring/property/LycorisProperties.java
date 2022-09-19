package eu.lycoris.spring.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ConfigurationProperties(prefix = "lycoris")
public class LycorisProperties {

  @Getter
  @Setter
  public static class AwsProperties {
    @Nullable private Integer maxNumberOfMessages;
    @Nullable private String secretKey;
    @Nullable private String accessKey;
    @Nullable private String region;
  }

  @Getter
  @Setter
  public static class CommandRetryProperties {
    @Nullable private Integer backoffMillisec = 15000;
    @Nullable private Integer delayMillisec = 10000;
    @Nullable private Integer maxAttempts = 4;
  }

  @Getter
  @Setter
  public static class SwaggerProperties {
    @Nullable private String basePackage;
  }

  @NotNull private final CommandRetryProperties commandRetry = new CommandRetryProperties();

  @NotNull private final AwsProperties secret = new AwsProperties();

  @NotNull private final AwsProperties s3 = new AwsProperties();

  @NotNull private final AwsProperties sns = new AwsProperties();

  @NotNull private final AwsProperties sqs = new AwsProperties();

  @NotNull private final SwaggerProperties swagger = new SwaggerProperties();
}
