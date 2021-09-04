package org.aguzman.test.springboot.app;

import org.aguzman.test.springboot.app.models.Bank;
import org.aguzman.test.springboot.app.models.BankAccount;

import java.math.BigDecimal;

public class Data {
//    public static final BankAccount ACCOUNT_001 = new BankAccount(1L, "Matt",new BigDecimal("1000"));
//    public static final BankAccount ACCOUNT_002 = new BankAccount(2L, "Tom",new BigDecimal("2000"));
//    public static final Bank BANK = new Bank(2L, "Citi",0);

    public static BankAccount createAccount001() {
        return new BankAccount(1L, "Matt",new BigDecimal("1000"));
    }

    public static BankAccount createAccount002() {
        return new BankAccount(2L, "Tom",new BigDecimal("2000"));
    }

    public static Bank createBank() {
        return new Bank(2L, "Citi",0);
    }

}
