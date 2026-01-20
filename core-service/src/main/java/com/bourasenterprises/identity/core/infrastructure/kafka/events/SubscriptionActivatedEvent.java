package com.bourasenterprises.identity.core.infrastructure.kafka.events;

import java.time.Instant;

public record SubscriptionActivatedEvent(
        String userId,
        String subscriptionId,
        Instant activatedAt
) {}