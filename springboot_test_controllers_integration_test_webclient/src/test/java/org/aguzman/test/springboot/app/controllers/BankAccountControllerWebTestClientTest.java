package org.aguzman.test.springboot.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aguzman.test.springboot.app.models.Bank;
import org.aguzman.test.springboot.app.models.BankAccount;
import org.aguzman.test.springboot.app.models.TransactionDto;
import org.apache.catalina.LifecycleState;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BankAccountControllerWebTestClientTest {
    
    /* in order to use WebClient, let's inject it */
    @Autowired
    private WebTestClient client;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    /* testing transfer method */
    @Order(1)
    @Test
    void testTransfer() throws JsonProcessingException {
        /* dto will be converted into a JSON automatically with WebTestClient, there is no need to use a ObjectMapper
        *  object */
        // given
        TransactionDto dto = new TransactionDto();
        dto.setOriginAccount(1L);
        dto.setDestinyAccount(2L);
        dto.setId(1L);
        dto.setAmount(new BigDecimal("100"));

        /* if needed, to test the content of the JSON, use this map from controller*/
        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status","OK");
        response.put("message","Successfully Transfer!");
        response.put("transaction",dto);

        // when
        client.post().uri("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                /* again, there is no need to use ObjectMapper */
                .bodyValue(dto)
                /* to send the request */
                .exchange()
        // then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)

                /* the body for default it will be byte, but you can define a new one, i.e: expectBody(String.class) */

                .expectBody()


                /* from here, you can choose consumeWith or jsonPath */

                /* 1. Using consumeWith. If jsonPath is not your fav, you can use the method 'consumeWith' */
                /* 1. 'answer' is the response of the server */
                .consumeWith(answer -> {
                    try {
                        JsonNode json = objectMapper.readTree(answer.getResponseBody());
                        assertEquals("Successfully Transfer!",json.path("message").asText());
                        assertEquals(1L,json.path("transaction").path("originAccount").asLong());
                        assertEquals(LocalDate.now().toString(),json.path("date").asText());
                        assertEquals("100",json.path("transaction").path("amount").asText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })

                /* 2. using jsonPath */
                .jsonPath("$.message").isNotEmpty()
                /* checking property using hamcrest */
                .jsonPath("$.message").value(is("Successfully Transfer!"))
                /* another possibility */
                .jsonPath("$.message").value(value -> assertEquals("Successfully Transfer!",value))
                .jsonPath("$.message").isEqualTo("Successfully Transfer!")
                .jsonPath("$.transaction.originAccount").isEqualTo(dto.getOriginAccount())
                .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
                /* to check the JSON as one entity, use json() no jsonPath() */
                /* keep in mind that you need to use an ObjectMapper object to do this */
                /* so 'response' should be converted to String using an ObjectMapper object */
                .json(objectMapper.writeValueAsString(response));
    }

    @Order(2)
    @Test
    void testDetail() throws JsonProcessingException {
        /* if I want to check the full expected JSON*/
        /* I will prepare an object in order to do eventually checj the mathc */
        BankAccount account = new BankAccount(1L,"Matt",new BigDecimal("900"));

        client.get().uri("/api/accounts/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isEqualTo("Matt")
                .jsonPath("$.credit").isEqualTo(900)
        /* this lines is to check the full JSON with my object 'account' */
        .json(objectMapper.writeValueAsString(account));
    }

    @Order(3)
    @Test
    void testDetail2() {
        client.get().uri("/api/accounts/2")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                /* now changing the body type of the JSON response to BankAccount */
                .expectBody(BankAccount.class)
                .consumeWith(response -> {
                    BankAccount account = response.getResponseBody();
                    assertNotNull(account);
                    assertEquals("Tom",account.getName());
                    assertEquals("2100.00",account.getCredit().toPlainString());
                });
    }

    @Order(4)
    @Test
    void testToList() {
        client.get().uri("/api/accounts").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                /* box brackets because is an array */
                .jsonPath("$[0].name").isEqualTo("Matt")
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].credit").isEqualTo(900)
                .jsonPath("$[1].name").isEqualTo("Tom")
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].credit").isEqualTo(2100)
                .jsonPath("$").isArray()
                .jsonPath("$").value(hasSize(2));
    }

    /* now using consumeWith */
    @Order(5)
    @Test
    void testToList2() {
        client.get().uri("/api/accounts").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                /* how do I want to expect the body of the response? */
                .expectBodyList(BankAccount.class)
                .consumeWith(response -> {
                    /* with the next line it will convert to a list */
                    List<BankAccount> accounts = response.getResponseBody();
                    /* now it is time to assertions */
                    assertNotNull(accounts);
                    assertEquals(2, accounts.size());
                    /* to obtain a specific value, get method is the way */
                    assertEquals(1L, accounts.get(0).getId());
                    assertEquals("Matt", accounts.get(0).getName());
                    assertEquals(900, accounts.get(0).getCredit().intValue());
                    assertEquals(2L, accounts.get(1).getId());
                    assertEquals("Tom", accounts.get(1).getName());
                    assertEquals(2100, accounts.get(1).getCredit().intValue());
                })
        /* using expectBodyList I can use method such: */
        .hasSize(2)
        .value(hasSize(2));
    }

    @Order(6)
    @Test
    void testSave() {
        // given
        BankAccount account = new BankAccount(null, "Angel",new BigDecimal("3000"));

        // when
        client.post().uri("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                /* automatically bodyValue will convert to JSON */
                .bodyValue(account)
                .exchange()
                // then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                /* first lets received as byte */
        .jsonPath("$.id").isEqualTo(3)
        .jsonPath("$.name").isEqualTo("Angel")
        .jsonPath("$.credit").isEqualTo(3000);

    }

    /* saving a new account, but now using consumeWith method */
    @Order(7)
    @Test
    void testSave2() {
        // given
        BankAccount account = new BankAccount(null, "Max",new BigDecimal("4000"));

        // when
        client.post().uri("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                /* automatically bodyValue will convert to JSON */
                .bodyValue(account)
                .exchange()
                // then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BankAccount.class)
        .consumeWith(response -> {
            BankAccount c = response.getResponseBody();
            assertNotNull(c);
            assertEquals(4L,c.getId());
            assertEquals("Max",c.getName());
            assertEquals("4000",c.getCredit().toPlainString());
        });
    }

    @Order(8)
    @Test
    void testDelete() {
        /* according with the current order there are 4 elements */
        /* so, besides to apply the delete, we can check the before/after state */

        client.get().uri("api/accounts").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(BankAccount.class)
                .hasSize(4);

        /* let's delete Angel */
        /* there is no content body to send inside the request */
        client.delete().uri("/api/accounts/3")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        /* after delete one element */
        client.get().uri("api/accounts").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(BankAccount.class)
                .hasSize(3);

        /* there is a chance to detect an error if there is a request for a specific path */
        client.get().uri("/api/accounts/3").exchange()
                .expectStatus().is5xxServerError();

        /* in case it is necessary to avoid a 5xx server error, there is another option
        * return a not found */
        /*
        .expectStatus().isNotFound()
                .expectBody().isEmpty();
         */
    }
}