package eu.lycoris.spring.configuration;

import java.util.Collections;

import org.springframework.cloud.aws.core.config.AmazonWebserviceClientFactoryBean;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.QueueMessageHandler;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.buffered.AmazonSQSBufferedAsyncClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.lycoris.spring.property.LycorisProperties;

@Configuration
public class LycorisSqsConfiguration {

  @Bean(destroyMethod = "shutdown")
  public AmazonSQS amazonSQS(LycorisProperties properties) throws Exception {
    AmazonWebserviceClientFactoryBean<AmazonSQSAsyncClient> clientFactoryBean =
        new AmazonWebserviceClientFactoryBean<>(
            AmazonSQSAsyncClient.class,
            new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(
                    properties.getSqs().getAccessKey(), properties.getSqs().getSecretKey())),
            () -> Region.getRegion(Regions.fromName(properties.getSqs().getRegion())));
    clientFactoryBean.afterPropertiesSet();
    return new AmazonSQSBufferedAsyncClient(clientFactoryBean.getObject());
  }

  @Bean
  public QueueMessageHandler queueMessageHandler(QueueMessageHandlerFactory messageHandlerFactory) {
    QueueMessageHandler handler = messageHandlerFactory.createQueueMessageHandler();
    handler.afterPropertiesSet();
    return handler;
  }

  @Bean
  public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(
      AmazonSQSAsync amazonSqs,
      QueueMessageHandler messageHandler,
      ThreadPoolTaskExecutor executor) {
    SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
    factory.setQueueMessageHandler(messageHandler);
    factory.setTaskExecutor(executor);
    factory.setAmazonSqs(amazonSqs);
    factory.setAutoStartup(true);
    return factory;
  }

  @Bean(destroyMethod = "destroy")
  public SimpleMessageListenerContainer simpleMessageListenerContainer(
      SimpleMessageListenerContainerFactory factory) throws Exception {
    SimpleMessageListenerContainer container = factory.createSimpleMessageListenerContainer();
    container.setMessageHandler(factory.getQueueMessageHandler());
    container.afterPropertiesSet();
    return container;
  }

  @Bean
  public QueueMessagingTemplate queueMessagingTemplate(
      AmazonSQSAsync amazonSqs, ObjectMapper objectMapper) {
    return new QueueMessagingTemplate(
        amazonSqs, (ResourceIdResolver) null, messageConverter(objectMapper));
  }

  @Bean
  public QueueMessageHandlerFactory queueMessageHandlerFactory(
      QueueMessagingTemplate queueMessagingTemplate, ObjectMapper objectMapper) {
    QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
    factory.setArgumentResolvers(
        Collections.<HandlerMethodArgumentResolver>singletonList(
            new PayloadArgumentResolver(messageConverter(objectMapper))));
    factory.setSendToMessagingTemplate(queueMessagingTemplate);
    return factory;
  }

  private MappingJackson2MessageConverter messageConverter(ObjectMapper objectMapper) {
    MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
    messageConverter.setSerializedPayloadClass(String.class);
    messageConverter.setStrictContentTypeMatch(false);
    messageConverter.setObjectMapper(objectMapper);
    return messageConverter;
  }
}
