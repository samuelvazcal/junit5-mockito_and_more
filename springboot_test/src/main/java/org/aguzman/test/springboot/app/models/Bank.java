package org.aguzman.test.springboot.app.models;

public class Bank {


    private Long id;
    private String bankName;
    private int totalTransfer;

    public Bank() {
    }

    public Bank(Long id, String bankName, int totalTransfer) {
        this.id = id;
        this.bankName = bankName;
        this.totalTransfer = totalTransfer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public int getTotalTransfer() {
        return totalTransfer;
    }

    public void setTotalTransfer(int totalTransfer) {
        this.totalTransfer = totalTransfer;
    }
}
