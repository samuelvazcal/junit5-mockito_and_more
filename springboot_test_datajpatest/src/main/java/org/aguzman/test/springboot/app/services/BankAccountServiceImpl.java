package org.aguzman.test.springboot.app.services;

import org.aguzman.test.springboot.app.models.Bank;
import org.aguzman.test.springboot.app.models.BankAccount;
import org.aguzman.test.springboot.app.repositories.BankAccountRepository;
import org.aguzman.test.springboot.app.repositories.BankRepository;

import java.math.BigDecimal;

public class BankAccountServiceImpl implements BankAccountService{

    private BankAccountRepository accountRepository;
    private BankRepository bankRepository;

    public BankAccountServiceImpl(BankAccountRepository accountRepository, BankRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    public BankAccount findById(Long id) {
        return accountRepository.findById(id).orElseThrow();
    }

    @Override
    public int checkTotalTransfer(Long idBank) {
        Bank bank = bankRepository.findById(idBank).orElseThrow();
        return bank.getTotalTransfer();
    }

    @Override
    public BigDecimal checkCredit(Long idBankAccount) {
        BankAccount account = accountRepository.findById(idBankAccount).orElseThrow();
        return account.getCredit();
    }

    @Override
    public void transfer(Long idOriginAccount, Long idDestinyAccount, BigDecimal credit, Long idBank) {

        BankAccount originAccount = accountRepository.findById(idOriginAccount).orElseThrow();
        originAccount.withdraw(credit);
        accountRepository.save(originAccount);

        BankAccount destinyAccount = accountRepository.findById(idDestinyAccount).orElseThrow();
        destinyAccount.deposit(credit);
        accountRepository.save(destinyAccount);

        Bank bank = bankRepository.findById(idBank).orElseThrow();
        int totalTransfer = bank.getTotalTransfer();
        bank.setTotalTransfer(++totalTransfer);
        bankRepository.save(bank);

    }
}
