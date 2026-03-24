package com.db1.orders.infra.messaging.idempotency;

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

    public String getOrderIdForKey(String idempotencyKey) {
        return repository.findByIdempotencyKey(idempotencyKey)
                .map(IdempotencyKeyEntity::getOrderId)
                .orElse(null);
    }

    public void storeIdempotencyKey(String idempotencyKey, String orderId) {
        IdempotencyKeyEntity entity = new IdempotencyKeyEntity(idempotencyKey, orderId);
        repository.save(entity);
        log.info("Armazenada chave de idempotência: {} para orderId: {}", idempotencyKey, orderId);
    }
}
