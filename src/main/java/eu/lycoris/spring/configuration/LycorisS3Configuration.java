package eu.lycoris.spring.configuration;

import io.awspring.cloud.core.config.AmazonWebserviceClientFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import eu.lycoris.spring.property.LycorisProperties;

@Configuration
public class LycorisS3Configuration {

    @Bean(destroyMethod = "shutdown")
    public AmazonS3 amazonS3(LycorisProperties properties) throws Exception {
        AmazonWebserviceClientFactoryBean<AmazonS3Client> clientFactoryBean =
                new AmazonWebserviceClientFactoryBean<>(
                        AmazonS3Client.class,
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(
                                        properties.getS3().getAccessKey(), properties.getS3().getSecretKey())),
                        () -> Region.getRegion(Regions.fromName(properties.getS3().getRegion())));
        clientFactoryBean.afterPropertiesSet();
        return clientFactoryBean.getObject();
    }
}
