package eu.lycoris.spring.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.xray.handlers.TracingHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.lycoris.spring.property.LycorisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.QueueMessageHandler;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Collections;

@Configuration
public class LycorisSqsConfiguration {

  @Bean(destroyMethod = "shutdown")
  public @NotNull AmazonSQSAsync amazonSQS(
      @NotNull LycorisProperties properties,
      @Nullable @Autowired(required = false) TracingHandler handler) {
    AmazonSQSAsyncClientBuilder builder =
        AmazonSQSAsyncClientBuilder.standard()
            .withCredentials(
                new AWSStaticCredentialsProvider(
                    new BasicAWSCredentials(
                        properties.getSqs().getAccessKey(), properties.getSqs().getSecretKey())))
            .withRegion(Regions.fromName(properties.getSqs().getRegion()));

    if (handler != null) {
      builder.withRequestHandlers(handler);
    }

    return builder.build();
  }

  @Bean
  public @NotNull QueueMessageHandler queueMessageHandler(
      @NotNull QueueMessageHandlerFactory messageHandlerFactory) {
    QueueMessageHandler handler = messageHandlerFactory.createQueueMessageHandler();
    handler.afterPropertiesSet();
    return handler;
  }

  @Bean
  public @NotNull SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(
      @NotNull AmazonSQSAsync amazonSqs,
      @NotNull QueueMessageHandler messageHandler,
      @NotNull LycorisProperties properties) {
    SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
    factory.setMaxNumberOfMessages(properties.getSqs().getMaxNumberOfMessages());
    factory.setQueueMessageHandler(messageHandler);
    factory.setAmazonSqs(amazonSqs);
    factory.setAutoStartup(true);
    return factory;
  }

  @Bean(destroyMethod = "destroy")
  public @NotNull SimpleMessageListenerContainer simpleMessageListenerContainer(
      SimpleMessageListenerContainerFactory factory) throws Exception {
    SimpleMessageListenerContainer container = factory.createSimpleMessageListenerContainer();
    container.setMessageHandler(factory.getQueueMessageHandler());
    container.afterPropertiesSet();
    return container;
  }

  @Bean
  public @NotNull QueueMessagingTemplate queueMessagingTemplate(
      @NotNull AmazonSQSAsync amazonSqs, @NotNull ObjectMapper objectMapper) {
    return new QueueMessagingTemplate(
        amazonSqs, (ResourceIdResolver) null, messageConverter(objectMapper));
  }

  @Bean
  public @NotNull QueueMessageHandlerFactory queueMessageHandlerFactory(
      @NotNull QueueMessagingTemplate queueMessagingTemplate, @NotNull ObjectMapper objectMapper) {
    QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
    factory.setArgumentResolvers(
        Collections.<HandlerMethodArgumentResolver>singletonList(
            new PayloadMethodArgumentResolver(messageConverter(objectMapper))));
    factory.setSendToMessagingTemplate(queueMessagingTemplate);
    return factory;
  }

  private MappingJackson2MessageConverter messageConverter(@NotNull ObjectMapper objectMapper) {
    MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
    messageConverter.setSerializedPayloadClass(String.class);
    messageConverter.setStrictContentTypeMatch(false);
    messageConverter.setObjectMapper(objectMapper);
    return messageConverter;
  }
}
