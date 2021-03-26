package com.cabral.myfirstmvvm.network.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserCompany {
    @PrimaryKey
    private int id;

    private String user_id;

    private String name;

    private String catchPhrase;

    private String bs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatchPhrase() {
        return catchPhrase;
    }

    public void setCatchPhrase(String catchPhrase) {
        this.catchPhrase = catchPhrase;
    }

    public String getBs() {
        return bs;
    }

    public void setBs(String bs) {
        this.bs = bs;
    }
}