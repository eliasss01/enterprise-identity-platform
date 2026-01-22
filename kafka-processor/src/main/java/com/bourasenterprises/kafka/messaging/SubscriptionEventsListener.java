package com.bourasenterprises.kafka.messaging;

import com.bourasenterprises.kafka.domain.events.SubscriptionActivatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SubscriptionEventsListener {

    @KafkaListener(topics = "subscription.activated")
    public void handle(SubscriptionActivatedEvent event) {
        log.info("Received event: {}", event);

        // simulazione provisioning
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}

        log.info("Provisioning completed for user {}", event.userId());
    }
}
