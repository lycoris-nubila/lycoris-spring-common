package eu.lycoris.spring.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerAsyncClient;
import eu.lycoris.spring.property.LycorisProperties;
import org.springframework.cloud.aws.core.config.AmazonWebserviceClientFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nullable;

@Configuration
public class LycorisSecretConfiguration {

  @Bean(destroyMethod = "shutdown")
  public @Nullable AWSSecretsManager amazonSecret(LycorisProperties properties) throws Exception {
    AmazonWebserviceClientFactoryBean<AWSSecretsManagerAsyncClient> clientFactoryBean =
        new AmazonWebserviceClientFactoryBean<>(
            AWSSecretsManagerAsyncClient.class,
            new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(
                    properties.getSecret().getAccessKey(), properties.getSecret().getSecretKey())),
            () -> Region.getRegion(Regions.fromName(properties.getSecret().getRegion())));
    clientFactoryBean.afterPropertiesSet();
    return clientFactoryBean.getObject();
  }
}
