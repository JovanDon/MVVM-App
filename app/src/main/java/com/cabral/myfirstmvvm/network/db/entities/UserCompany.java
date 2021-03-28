package com.cabral.myfirstmvvm.network.db.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "UserCompany",foreignKeys = {
        @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = "user_id")
        })
public class UserCompany {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int user_id;

    private String name;

    private String catchPhrase;

    private String bs;

    public UserCompany(int user_id, String name, String catchPhrase, String bs) {
        this.user_id = user_id;
        this.name = name;
        this.catchPhrase = catchPhrase;
        this.bs = bs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
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