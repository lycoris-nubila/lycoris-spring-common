package eu.lycoris.spring.sns;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.message.SnsMessageManager;

import eu.lycoris.spring.common.LycorisSubjectMessage;
import eu.lycoris.spring.property.LycorisProperties;

@Component
public class LycorisSnsManager extends SnsMessageManager {

  private NotificationMessagingTemplate notifier;

  @Autowired
  public LycorisSnsManager(LycorisProperties properties, AmazonSNS amazonSns) {
    super(properties.getSns().getRegion());
    this.notifier = new NotificationMessagingTemplate(amazonSns);
  }

  public void sendMessage(String snsTopic, LycorisSubjectMessage message) {
    notifier.sendNotification(snsTopic, message, message.getSubject());
  }
}
