package com.example.tugasakhir.model.user;

import com.google.gson.annotations.SerializedName;

public class UserBody {
    @SerializedName("uid")
    private String uid;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    public UserBody(String uid, String email, String name) {
        this.uid = uid;
        this.email = email;
        this.name = name;
    }

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
}
