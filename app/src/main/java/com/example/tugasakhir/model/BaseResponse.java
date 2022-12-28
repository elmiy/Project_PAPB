package com.example.tugasakhir.model;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BaseResponse<T> {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("count")
    @Nullable
    private Integer count;

    @SerializedName("data")
    @Nullable
    private T data;

    @NotNull
    public final String getStatus() {
        return this.status;
    }

    @NotNull
    public final String getMessage() {
        return this.message;
    }

    @Nullable
    public final Integer getCount() {
        return this.count;
    }

    @Nullable
    public final T getData() {
        return this.data;
    }
}
