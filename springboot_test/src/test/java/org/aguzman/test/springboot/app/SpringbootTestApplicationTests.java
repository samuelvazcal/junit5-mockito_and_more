package org.aguzman.test.springboot.app;

import org.aguzman.test.springboot.app.exceptions.NotEnoughFundsException;
import org.aguzman.test.springboot.app.models.Bank;
import org.aguzman.test.springboot.app.models.BankAccount;
import org.aguzman.test.springboot.app.repositories.BankAccountRepository;
import org.aguzman.test.springboot.app.repositories.BankRepository;
import org.aguzman.test.springboot.app.services.BankAccountService;
import org.aguzman.test.springboot.app.services.BankAccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.aguzman.test.springboot.app.Data.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SpringbootTestApplicationTests {

	@Mock
	BankAccountRepository accountRepository;

	@Mock
	BankRepository bankRepository;

	BankAccountService service;

	@BeforeEach
	void setUp() {
		/*now, using annotations*/
		accountRepository = mock(BankAccountRepository.class);
		bankRepository = mock(BankRepository.class);
		service = new BankAccountServiceImpl(accountRepository,bankRepository);
	}

	@Test
	void contextLoads() {
		when(accountRepository.findById(1L)).thenReturn(createAccount001());
		when(accountRepository.findById(2L)).thenReturn(createAccount002());
		when(bankRepository.findById(1L)).thenReturn(createBank());

		// testing checkCredit ...
		BigDecimal creditOrigin = service.checkCredit(1L);
		BigDecimal creditDestiny = service.checkCredit(2L);

		assertEquals("1000", creditOrigin.toPlainString());
		assertEquals("2000", creditDestiny.toPlainString());

		service.transfer(1L,2L,new BigDecimal("100"),1L);
		creditOrigin = service.checkCredit(1L);
		creditDestiny = service.checkCredit(2L);

		assertEquals("900", creditOrigin.toPlainString());
		assertEquals("2100", creditDestiny.toPlainString());

		// testing checkTotalTransfer...
		int total = service.checkTotalTransfer(1L);
		assertEquals(1,total);

		// times that findById is executed in this test
		verify(accountRepository,times(3)).findById(1L);
		verify(accountRepository,times(3)).findById(2L);

		verify(accountRepository,times(2)).update(any(BankAccount.class));

		verify(bankRepository,times(2)).findById(1L);
		verify(bankRepository).update(any(Bank.class));

		verify(accountRepository,never()).findAll();
		verify(accountRepository,times(6)).findById(anyLong());
	}

	@Test
	void contextLoads2() {
		when(accountRepository.findById(1L)).thenReturn(createAccount001());
		when(accountRepository.findById(2L)).thenReturn(createAccount002());
		when(bankRepository.findById(1L)).thenReturn(createBank());

		// testing checkCredit ...
		BigDecimal creditOrigin = service.checkCredit(1L);
		BigDecimal creditDestiny = service.checkCredit(2L);

		assertEquals("1000", creditOrigin.toPlainString());
		assertEquals("2000", creditDestiny.toPlainString());

		assertThrows(NotEnoughFundsException.class, () -> {
			service.transfer(1L,2L,new BigDecimal("1200"),1L);
		});

		creditOrigin = service.checkCredit(1L);
		creditDestiny = service.checkCredit(2L);

		assertEquals("1000", creditOrigin.toPlainString());
		assertEquals("2000", creditDestiny.toPlainString());

		// testing checkTotalTransfer...
		int total = service.checkTotalTransfer(1L);
		assertEquals(0,total);

		// times that findById is executed in this test
		verify(accountRepository,times(3)).findById(1L);
		verify(accountRepository,times(2)).findById(2L);

		verify(accountRepository,never()).update(any(BankAccount.class));

		verify(bankRepository,times(1)).findById(1L);
		verify(bankRepository,never()).update(any(Bank.class));

		verify(accountRepository,never()).findAll();
		verify(accountRepository,times(5)).findById(anyLong());

	}

	@Test
	void contextLoads3() {
		when(accountRepository.findById(1L)).thenReturn(createAccount001());

		BankAccount account1 = service.findById(1L);
		BankAccount account2 = service.findById(1L);

		assertSame(account1,account2);
		assertEquals("Matt",account1.getName());
		assertEquals("Matt",account2.getName());


	}

}
