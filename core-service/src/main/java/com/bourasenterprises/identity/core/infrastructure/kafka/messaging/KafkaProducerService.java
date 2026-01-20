package com.bourasenterprises.identity.core.infrastructure.kafka.messaging;

import com.bourasenterprises.identity.core.infrastructure.kafka.events.SubscriptionActivatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, SubscriptionActivatedEvent> kafkaTemplate;

    public void publishSubscriptionActivated(SubscriptionActivatedEvent event){
        kafkaTemplate.send("subscription.activated", event.userId(), event);
    }
}
