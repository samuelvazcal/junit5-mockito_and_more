package org.aguzman.test.springboot.app.models;

import java.math.BigDecimal;

public class TransactionDto {
    private Long originAccount;
    private Long destinyAccount;
    private BigDecimal amount;
    private Long id;


    public Long getOriginAccount() {
        return originAccount;
    }

    public void setOriginAccount(Long originAccount) {
        this.originAccount = originAccount;
    }

    public Long getDestinyAccount() {
        return destinyAccount;
    }

    public void setDestinyAccount(Long destinyAccount) {
        this.destinyAccount = destinyAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
