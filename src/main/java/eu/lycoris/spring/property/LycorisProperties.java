package eu.lycoris.spring.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "lycoris")
public class LycorisProperties {

  @Getter
  @Setter
  public static class AwsProperties {
    private Integer maxNumberOfMessages;
    private String secretKey;
    private String accessKey;
    private String region;
  }

  @Getter
  @Setter
  public static class CommandRetryProperties {
    private Integer backoffMillisec = 15000;
    private Integer delayMillisec = 10000;
    private Integer maxAttempts = 4;
  }

  @Getter
  @Setter
  public static class SwaggerProperties {
    private String basePackage;
  }

  private final CommandRetryProperties commandRetry = new CommandRetryProperties();

  private final AwsProperties secret = new AwsProperties();

  private final AwsProperties s3 = new AwsProperties();

  private final AwsProperties sns = new AwsProperties();

  private final AwsProperties sqs = new AwsProperties();

  private final SwaggerProperties swagger = new SwaggerProperties();
}
