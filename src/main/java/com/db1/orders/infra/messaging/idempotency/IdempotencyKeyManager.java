package com.db1.orders.infra.messaging.idempotency;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.db1.orders.infra.persistence.entity.IdempotencyKeyEntity;
import com.db1.orders.infra.persistence.repository.SpringDataIdempotencyKeyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class IdempotencyKeyManager {

    private final SpringDataIdempotencyKeyRepository repository;

    public UUID getOrderIdForKey(String idempotencyKey) {
        return repository.findByIdempotencyKey(idempotencyKey)
                .map(IdempotencyKeyEntity::getOrderId)
                .orElse(null);
    }

    public void storeIdempotencyKey(String idempotencyKey, UUID orderId) {
        IdempotencyKeyEntity entity = new IdempotencyKeyEntity(idempotencyKey, orderId);
        repository.save(entity);
        log.info("Armazenada chave de idempotência: {} para orderId: {}", idempotencyKey, orderId);
    }
}
