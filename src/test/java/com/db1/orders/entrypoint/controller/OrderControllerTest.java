package com.db1.orders.entrypoint.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.db1.orders.infra.AbstractIntegrationTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateOrderAndReturn201() throws Exception {
        String body = """
                {
                    "customerId": "CUST-API-1",
                    "orderId": "ORD-API-001",
                    "items": [
                        {
                            "sku": "SN-API-1",
                            "quantity": "3",
                            "unitPrice": 15.50
                        }
                    ]
                }
                """;

        mockMvc.perform(post("/orders")
                .header("Idempotency-Key", "api-idem-001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value("ORD-API-001"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void shouldReturnSameResponseForDuplicateIdempotencyKey() throws Exception {
        String body = """
                {
                    "customerId": "CUST-API-2",
                    "orderId": "ORD-API-002",
                    "items": [
                        {
                            "sku": "SN-API-2",
                            "quantity": "1",
                            "unitPrice": 10.00
                        }
                    ]
                }
                """;

        MvcResult first = mockMvc.perform(post("/orders")
                .header("Idempotency-Key", "api-idem-dup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        MvcResult second = mockMvc.perform(post("/orders")
                .header("Idempotency-Key", "api-idem-dup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode firstJson = objectMapper.readTree(first.getResponse().getContentAsString());
        JsonNode secondJson = objectMapper.readTree(second.getResponse().getContentAsString());

        assert firstJson.get("id").asText().equals(secondJson.get("id").asText());
    }

    @Test
    void shouldReturn400WhenIdempotencyKeyMissing() throws Exception {
        String body = """
                {
                    "customerId": "CUST-API-3",
                    "orderId": "ORD-API-003",
                    "items": [
                        {
                            "sku": "SN-API-3",
                            "quantity": "1",
                            "unitPrice": 5.00
                        }
                    ]
                }
                """;

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetOrderById() throws Exception {
        String body = """
                {
                    "customerId": "CUST-API-4",
                    "orderId": "ORD-API-004",
                    "items": [
                        {
                            "sku": "SN-API-4",
                            "quantity": "2",
                            "unitPrice": 20.00
                        }
                    ]
                }
                """;

        MvcResult createResult = mockMvc.perform(post("/orders")
                .header("Idempotency-Key", "api-idem-get")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        String orderId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("orderId").asText();

        mockMvc.perform(get("/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("ORD-API-004"))
                .andExpect(jsonPath("$.customerId").value("CUST-API-4"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].sku").value("SN-API-4"));
    }

    @Test
    void shouldReturn404ForNonExistentOrder() throws Exception {
        mockMvc.perform(get("/orders/32132123123123"))
                .andExpect(status().isNotFound());
    }
}
