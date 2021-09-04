package org.aguzman.test.springboot.app.repositories;

import org.aguzman.test.springboot.app.models.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount,Long> {
    Optional<BankAccount> findByUserName(String userName);
    //List<BankAccount> findAll();
    //BankAccount findById(Long id);
    //void update(BankAccount bankAccount);
}
