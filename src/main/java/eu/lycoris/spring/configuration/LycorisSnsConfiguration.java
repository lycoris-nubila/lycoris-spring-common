package eu.lycoris.spring.configuration;

import io.awspring.cloud.core.config.AmazonWebserviceClientFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSAsyncClient;

import eu.lycoris.spring.property.LycorisProperties;

@Configuration
@ComponentScan("eu.lycoris.spring.sns")
public class LycorisSnsConfiguration {

    @Bean(destroyMethod = "shutdown")
    public AmazonSNS amazonSns(LycorisProperties properties) throws Exception {
        AmazonWebserviceClientFactoryBean<AmazonSNSAsyncClient> clientFactoryBean =
                new AmazonWebserviceClientFactoryBean<>(
                        AmazonSNSAsyncClient.class,
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(
                                        properties.getSns().getAccessKey(), properties.getSns().getSecretKey())),
                        () -> Region.getRegion(Regions.fromName(properties.getSns().getRegion())));
        clientFactoryBean.afterPropertiesSet();
        return clientFactoryBean.getObject();
    }
}
