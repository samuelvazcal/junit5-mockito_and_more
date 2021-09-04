package org.aguzman.test.springboot.app.services;

import org.aguzman.test.springboot.app.models.BankAccount;

import java.math.BigDecimal;
import java.util.List;

public interface BankAccountService {
    List<BankAccount> findAll();

    BankAccount findById(Long id);

    BankAccount save(BankAccount account);

    void deleteById(Long id);

    int checkTotalTransfer(Long idBank);
    BigDecimal checkCredit(Long idBankAccount);
    void transfer(Long idOriginAccount, Long idDestinyAccount, BigDecimal credit, Long idBank);
}
