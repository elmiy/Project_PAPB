package com.example.tugasakhir.model.transaction;

import com.google.gson.annotations.SerializedName;

public class TransactionBody {
    @SerializedName("uid")
    private String uid;

    @SerializedName("amount")
    private int amount;

    @SerializedName("type")
    private String type;

    @SerializedName("description")
    private String description;

    @SerializedName("date")
    private String date;

    public TransactionBody(String uid, int amount, String type, String description, String date) {
        this.uid = uid;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.date = date;
    }
}
