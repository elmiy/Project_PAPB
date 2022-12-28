package com.example.tugasakhir.util;

public enum TransactionType {

    INCOME("Income"),
    EXPENSE("Expense");

    private String type;

    TransactionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
