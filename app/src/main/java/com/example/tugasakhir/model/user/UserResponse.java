package com.example.tugasakhir.model.user;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

    @SerializedName("uid")
    private String uid;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("avatar")
    private String avatar;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }
}
