package com.cabral.myfirstmvvm.network.db.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_posts")
public class UserPostEntity implements Parcelable {
    @PrimaryKey
    private int id;

    private int userId;

    private String title;

    private String body;

    public UserPostEntity(int id, int userId, String title, String body) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.body = body;
    }

    protected UserPostEntity(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        title = in.readString();
        body = in.readString();
    }

    public static final Creator<UserPostEntity> CREATOR = new Creator<UserPostEntity>() {
        @Override
        public UserPostEntity createFromParcel(Parcel in) {
            return new UserPostEntity(in);
        }

        @Override
        public UserPostEntity[] newArray(int size) {
            return new UserPostEntity[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setUser_id(int user_id) {
        this.userId = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getUserId() {
        return userId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeString(title);
        dest.writeString(body);
    }
}
