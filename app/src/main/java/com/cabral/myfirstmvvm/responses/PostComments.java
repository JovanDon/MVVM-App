package com.cabral.myfirstmvvm.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostComments {
    @SerializedName("postId")
    @Expose
    private String postId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private int name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("body")
    @Expose
    private String body;
}
