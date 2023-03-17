package org.khasanof.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.khasanof.dto.transaction.TransactionCardGetDTO;
import org.khasanof.dto.transaction.TransactionMultiCardGetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/17/2023
 * <br/>
 * Time: 10:48 PM
 * <br/>
 * Package: org.khasanof.web.rest
 */
@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class TransactionResourceTest {

    @Autowired
    private WebTestClient client;

    @BeforeEach
    void setUp() {
        client = client.mutate()
            .responseTimeout(Duration.ofMillis(30000))
            .build();
    }

    @Test
    void shouldGetTransactionsWithCard() {
        String cardNumber = "5425764309411081";
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 2, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 4, 0, 0);

        TransactionCardGetDTO dto = new TransactionCardGetDTO(cardNumber, from1, to1);

        client.post()
            .uri("http://localhost:8081/api/transactionsWithCard")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(dto)
            .exchange()
            .expectStatus()
            .isOk();
    }

    @Test
    void shouldGetTransactionsWithMultiCards() {
        String cardNumber1 = "5425764309411081";
        String cardNumber2 = "8600809850688759";
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 3, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 6, 0, 0);

        TransactionMultiCardGetDTO dto = new TransactionMultiCardGetDTO(List.of(cardNumber1, cardNumber2), from1, to1);

        client.post()
            .uri("http://localhost:8081/api/transactionsWithMultiCards")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(dto)
            .exchange()
            .expectStatus()
            .isOk();

    }

    @Test
    void shouldGetTransactionsWithMultiCardsAndJsonPath() {
        String cardNumber1 = "5425764309411081";
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 3, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 4, 0, 0);

        TransactionCardGetDTO dto = new TransactionCardGetDTO(cardNumber1, from1, to1);

        client.post()
            .uri("http://localhost:8081/api/transactionsWithCard")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(dto)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .jsonPath("$.[0].id").isEqualTo("1")
            .jsonPath("$.[0].amount").isEqualTo("2692852.2")
            .jsonPath("$.[0].status").isEqualTo("FAILED")
            .jsonPath("$.[1].id").isEqualTo("3")
            .jsonPath("$.[1].amount").isEqualTo("3256944.51")
            .jsonPath("$.[1].status").isEqualTo("FAILED");

    }
}
