package com.cabral.myfirstmvvm.network.db.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "post_comment",foreignKeys = {
        @ForeignKey(entity = UserPostEntity.class,
                parentColumns = "id",
                childColumns = "postId",
                onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = "postId")
        })
public class PostComment implements Parcelable {
    @PrimaryKey
    private int id;

    private int postId;

    private String name;

    private String email;

    private String body;

    public PostComment(int id, int postId, String name, String email, String body) {
        this.id = id;
        this.postId = postId;
        this.name = name;
        this.email = email;
        this.body = body;
    }

    protected PostComment(Parcel in) {
        id = in.readInt();
        postId = in.readInt();
        name = in.readString();
        email = in.readString();
        body = in.readString();
    }

    public static final Creator<PostComment> CREATOR = new Creator<PostComment>() {
        @Override
        public PostComment createFromParcel(Parcel in) {
            return new PostComment(in);
        }

        @Override
        public PostComment[] newArray(int size) {
            return new PostComment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(postId);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(body);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
