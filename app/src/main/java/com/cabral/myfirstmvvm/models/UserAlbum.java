package com.cabral.myfirstmvvm.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAlbum {
    @SerializedName("userId")
    @Expose
    private int user_id;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
}
