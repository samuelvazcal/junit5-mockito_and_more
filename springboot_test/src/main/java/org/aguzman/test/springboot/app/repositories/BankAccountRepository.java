package org.aguzman.test.springboot.app.repositories;

import org.aguzman.test.springboot.app.models.BankAccount;

import java.util.List;

public interface BankAccountRepository {
    List<BankAccount> findAll();
    BankAccount findById(Long id);
    void update(BankAccount bankAccount);
}
