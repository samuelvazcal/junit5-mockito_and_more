package org.aguzman.test.springboot.app.repositories;

import org.aguzman.test.springboot.app.models.Bank;

import java.awt.image.BandCombineOp;
import java.util.List;

public interface BankRepository {
    List<Bank> findAll();
    Bank findById(Long id);
    void update(Bank bank);

}
