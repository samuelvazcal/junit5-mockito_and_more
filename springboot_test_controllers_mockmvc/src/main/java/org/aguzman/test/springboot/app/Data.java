package org.aguzman.test.springboot.app;

import org.aguzman.test.springboot.app.models.Bank;
import org.aguzman.test.springboot.app.models.BankAccount;

import java.math.BigDecimal;
import java.util.Optional;

public class Data {
//    public static final BankAccount ACCOUNT_001 = new BankAccount(1L, "Matt",new BigDecimal("1000"));
//    public static final BankAccount ACCOUNT_002 = new BankAccount(2L, "Tom",new BigDecimal("2000"));
//    public static final Bank BANK = new Bank(2L, "Citi",0);

    public static Optional<BankAccount> createAccount001() {
        return Optional.of(new BankAccount(1L, "Matt",new BigDecimal("1000")));
    }

    public static Optional<BankAccount> createAccount002() {
        return Optional.of(new BankAccount(2L, "Tom",new BigDecimal("2000")));
    }

    public static Optional<Bank> createBank() {
        return Optional.of(new Bank(1L, "Citi",0));
    }

}
