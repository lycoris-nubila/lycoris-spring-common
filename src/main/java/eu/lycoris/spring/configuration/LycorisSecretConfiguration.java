package eu.lycoris.spring.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerAsync;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerAsyncClientBuilder;
import com.amazonaws.xray.handlers.TracingHandler;
import eu.lycoris.spring.property.LycorisProperties;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LycorisSecretConfiguration {

  @Bean(destroyMethod = "shutdown")
  public @NotNull AWSSecretsManagerAsync amazonSecret(
      @NotNull LycorisProperties properties,
      @Nullable @Autowired(required = false) TracingHandler handler) {
    AWSSecretsManagerAsyncClientBuilder builder =
        AWSSecretsManagerAsyncClientBuilder.standard()
            .withCredentials(
                new AWSStaticCredentialsProvider(
                    new BasicAWSCredentials(
                        properties.getSecret().getAccessKey(),
                        properties.getSecret().getSecretKey())))
            .withRegion(Regions.fromName(properties.getSecret().getRegion()));

    if (handler != null) {
      builder.withRequestHandlers(handler);
    }

    return builder.build();
  }
}
