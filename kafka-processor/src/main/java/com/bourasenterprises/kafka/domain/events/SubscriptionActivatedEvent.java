package com.bourasenterprises.kafka.domain.events;

import java.time.Instant;

public record SubscriptionActivatedEvent(
        String userId,
        String subscriptionId,
        Instant activatedAt
) {}