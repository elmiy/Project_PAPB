package com.example.tugasakhir.model.transaction;

import com.google.gson.annotations.SerializedName;

public class TransactionResponse {
    @SerializedName("uid")
    private String uid;

    @SerializedName("transaction_id")
    private String transactionId;

    @SerializedName("amount")
    private int amount;

    @SerializedName("type")
    private String type;

    @SerializedName("description")
    private String description;

    @SerializedName("date")
    private String date;

    public String getUid() {
        return uid;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public int getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
}
