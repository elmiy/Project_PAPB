package com.example.tugasakhir.model.wallet;

import com.google.gson.annotations.SerializedName;

public class WalletResponse {
    @SerializedName("uid")
    private String uid;

    @SerializedName("income")
    private int income;

    @SerializedName("expense")
    private int expense;

    public String getUid() {
        return uid;
    }

    public int getIncome() {
        return income;
    }

    public int getExpense() {
        return expense;
    }
}
