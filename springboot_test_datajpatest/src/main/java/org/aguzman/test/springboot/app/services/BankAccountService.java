package org.aguzman.test.springboot.app.services;

import org.aguzman.test.springboot.app.models.BankAccount;

import java.math.BigDecimal;

public interface BankAccountService {
    BankAccount findById(Long id);
    int checkTotalTransfer(Long idBank);
    BigDecimal checkCredit(Long idBankAccount);
    void transfer(Long idOriginAccount, Long idDestinyAccount, BigDecimal credit, Long idBank);
}
