package org.aguzman.test.springboot.app.services;

import org.aguzman.test.springboot.app.models.Bank;
import org.aguzman.test.springboot.app.models.BankAccount;
import org.aguzman.test.springboot.app.repositories.BankAccountRepository;
import org.aguzman.test.springboot.app.repositories.BankRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BankAccountServiceImpl implements BankAccountService{

    private BankAccountRepository accountRepository;
    private BankRepository bankRepository;

    public BankAccountServiceImpl(BankAccountRepository accountRepository, BankRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    @Transactional
    public List<BankAccount> findAll() {
        return accountRepository.findAll();
    }

    @Override
    @Transactional
    public BankAccount findById(Long id) {
        return accountRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public BankAccount save(BankAccount account) {
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    @Transactional
    public int checkTotalTransfer(Long idBank) {
        Bank bank = bankRepository.findById(idBank).orElseThrow();
        return bank.getTotalTransfer();
    }

    @Override
    @Transactional
    public BigDecimal checkCredit(Long idBankAccount) {
        BankAccount account = accountRepository.findById(idBankAccount).orElseThrow();
        return account.getCredit();
    }

    @Override
    @Transactional
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
