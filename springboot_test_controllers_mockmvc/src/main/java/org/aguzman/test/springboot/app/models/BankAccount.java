package org.aguzman.test.springboot.app.models;

import org.aguzman.test.springboot.app.exceptions.NotEnoughFundsException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "bank_accounts")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "credit")
    private BigDecimal credit;

    public BankAccount() {
    }

    public BankAccount(Long id, String userName, BigDecimal credit) {
        this.id = id;
        this.userName = userName;
        this.credit = credit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return userName;
    }

    public void setName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public void withdraw(BigDecimal credit) {
        BigDecimal newCredit = this.credit.subtract(credit);
        if(newCredit.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughFundsException("Not enough fund in your account");
        }
        this.credit = newCredit;
    }

    public void deposit(BigDecimal credit) {
        this.credit = this.credit.add(credit);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(credit, that.credit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, credit);
    }
}
