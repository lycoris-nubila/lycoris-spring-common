package eu.lycoris.spring.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "lycoris")
public class LycorisProperties {

  private final SwaggerProperties swagger = new SwaggerProperties();

  private final AwsProperties sns = new AwsProperties();

  private final AwsProperties sqs = new AwsProperties();

  @Getter
  @Setter
  public static class SwaggerProperties {
    private String basePackage;
  }

  @Getter
  @Setter
  public static class AwsProperties {
    private String secretKey;
    private String accessKey;
    private String region;
  }
}
