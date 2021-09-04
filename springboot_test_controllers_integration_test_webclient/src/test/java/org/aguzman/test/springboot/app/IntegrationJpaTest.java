package org.aguzman.test.springboot.app;

import org.aguzman.test.springboot.app.models.BankAccount;
import org.aguzman.test.springboot.app.repositories.BankAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/* Applying test on DAO */
@DataJpaTest
public class IntegrationJpaTest {

    @Autowired
    BankAccountRepository accountRepository;


    @Test
    void testFindById() {
        Optional<BankAccount> foundAccount = accountRepository.findById(1L);
        assertTrue(foundAccount.isPresent());
        assertEquals("Matt",foundAccount.orElseThrow().getName());
    }

    @Test
    void testFindByUserName() {
        Optional<BankAccount> foundAccount = accountRepository.findByUserName("Tom");
        assertTrue(foundAccount.isPresent());
        assertEquals("Tom",foundAccount.orElseThrow().getName());
        assertEquals("2000.00",foundAccount.orElseThrow().getCredit().toPlainString());
    }

    @Test
    void testFindByUserNameThrowException() {
        Optional<BankAccount> foundAccount = accountRepository.findByUserName("Tim");
        assertThrows(NoSuchElementException.class, foundAccount::orElseThrow);
        assertFalse(foundAccount.isPresent());
    }

    @Test
    void testFindAll() {
        List<BankAccount> accounts = accountRepository.findAll();
        assertFalse(accounts.isEmpty());
        assertEquals(2,accounts.size());
    }

    @Test
    void testSave() {
        //given
        BankAccount account = new BankAccount(null,"Ray",new BigDecimal("3000"));
        BankAccount accountSaved = accountRepository.save(account);

        //when
        //BankAccount foundAccount1 = accountRepository.findByUserName("Ray").orElseThrow();
        BankAccount foundAccount = accountRepository.findById(accountSaved.getId()).orElseThrow();


        assertEquals("Ray",foundAccount.getName());
        assertEquals("3000",foundAccount.getCredit().toPlainString());
        //assertEquals(3,foundAccount.getId());
    }


    @Test
    void testUpdate() {
        //given
        BankAccount accountRay = new BankAccount(null,"Ray",new BigDecimal("3000"));

        //when
        BankAccount accountSaved = accountRepository.save(accountRay);
        //BankAccount foundAccount1 = accountRepository.findByUserName("Ray").orElseThrow();
        //BankAccount foundAccount = accountRepository.findById(accountSaved.getId()).orElseThrow();


        assertEquals("Ray",accountSaved.getName());
        assertEquals("3000",accountSaved.getCredit().toPlainString());
        //assertEquals(3,foundAccount.getId());

        /* with a saved account, now, it is time to update some property, such credit */
        // when
        accountSaved.setCredit(new BigDecimal("3200"));
        BankAccount accountUpdated = accountRepository.save(accountSaved);

        /* time to check the update for the respective account */
        // then
        assertEquals("Ray",accountUpdated.getName());
        assertEquals("3200",accountUpdated.getCredit().toPlainString());

    }

    @Test
    void testDelete() {
        /* in order to delete an account, first, it is necessary to find the account */
        // given
        BankAccount account = accountRepository.findById(2L).orElseThrow();
        assertEquals("Tom",account.getName());

        // when
        accountRepository.delete(account);

        /* it was deleted, now to confirm that was properly deleted one way is to receive an exception */
        assertThrows(NoSuchElementException.class, () -> accountRepository.findByUserName("Tom").orElseThrow());

        /* another possibility is to check the size of the list */
        assertEquals(1,accountRepository.findAll().size());
    }
}
