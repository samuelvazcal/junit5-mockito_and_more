package org.aguzman.test.springboot.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aguzman.test.springboot.app.Data;
import org.aguzman.test.springboot.app.models.BankAccount;
import org.aguzman.test.springboot.app.models.TransactionDto;
import org.aguzman.test.springboot.app.services.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.aguzman.test.springboot.app.Data.createAccount001;
import static org.aguzman.test.springboot.app.Data.createAccount002;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BankAccountController.class)
class BankAccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BankAccountService accountService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    /* testing method 'detail' from BankAccountController*/
    @Test
    void testDetail() throws Exception {
        // given
        when(accountService.findById(1L)).thenReturn(createAccount001().orElseThrow());

        /* here the call to the controller will be launched */
        // when
        mvc.perform(get("/api/accounts/1").contentType(MediaType.APPLICATION_JSON))

        // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                /* it will return a BankAccount instance, so properties can be tested */
                .andExpect(jsonPath("$.name").value("Matt"))
                .andExpect(jsonPath("$.credit").value("1000"));


        verify(accountService).findById(1L);
    }

    /* testing method "transfer */

    @Test
    void testTransfer() throws Exception {
        /* it will be a POST operation, we need to send a body for our request (RequestBody) */
        /* let's create a dto, and then let's convert it into a JSON using an ObjectMapper */
        // given
        TransactionDto dto = new TransactionDto();
        dto.setOriginAccount(1L);
        dto.setDestinyAccount(2L);
        dto.setAmount(new BigDecimal("100"));
        dto.setId(1L);


        /* just in case you want to check the content of the JSON 'response' */
        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status","OK");
        response.put("message","Successfully Transfer!");
        response.put("transaction",dto);
        System.out.println((objectMapper.writeValueAsString(response)));

        //when
        mvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)))

        // then
        .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.message").value("Successfully Transfer!"))
                .andExpect(jsonPath("$.transaction.originAccount").value(1L))
        .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    /* testing toList method from BankAccountController */
    @Test
    void testToList() throws Exception {
        // given
        List<BankAccount> accounts = Arrays.asList(createAccount001().orElseThrow(),createAccount002().orElseThrow());

        /* when findAll method from BankAccountServiceImpl run, it will return 'accounts' */
        /* we are testing the controller, so we don't need an implementation for the BankAccountService */

        when(accountService.findAll()).thenReturn(accounts);

        // when
        mvc.perform(get("/api/accounts").contentType(MediaType.APPLICATION_JSON))
        // then
        .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                /* jsonPath will return an array, for this case will return two elements */
                .andExpect(jsonPath("$[0].name").value("Matt"))
                .andExpect(jsonPath("$[1].name").value("Tom"))
                .andExpect(jsonPath("$[0].credit").value("1000"))
                .andExpect(jsonPath("$[1].credit").value("2000"))
                /* checking the size of the array */
                .andExpect(jsonPath("$",hasSize(2)))
                /* lets check (also) if the content of the initial list 'accounts' is equal to the json content */
                .andExpect(content().json(objectMapper.writeValueAsString(accounts)));
    }

    /* now testing the save method from the controller */

    @Test
    void testSave() throws Exception {
        // given
        BankAccount account = new BankAccount(null, "Sam", new BigDecimal("3000"));
        /* any Account type will return 'account' */
        //when(accountService.save(any())).thenReturn(account);
        when(accountService.save(any())).then(invocationOnMock -> {
            BankAccount c = invocationOnMock.getArgument(0);
            c.setId(3L);
            return c;
        });

        // when
        mvc.perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(account)))

        // then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                /* if I want to test the id, right now is not possible, because is null and eventually the assigned
                id can change. We can use a lambda expression to catch the object and assign a specific id. so line
                142 it will be commented and the next line is the appropriate implementation for this case */
                .andExpect(jsonPath("$.id").value(3))
                /* to diversify the way of checking result let's use Matchers */
                .andExpect(jsonPath("$.name",is("Sam")))
                .andExpect(jsonPath("$.credit",is(3000)));

                /* checking if the 'save' method is calling with any BankAccount object */
        verify(accountService).save(any());
    }

}