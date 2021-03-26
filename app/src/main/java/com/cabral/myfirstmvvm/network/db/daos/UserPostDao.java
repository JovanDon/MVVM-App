package com.cabral.myfirstmvvm.network.db.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cabral.myfirstmvvm.models.UserDetails;
import com.cabral.myfirstmvvm.network.db.entities.UserPost;

import java.util.List;

@Dao
public interface UserPostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserPost userPost);


    @Query("SELECT user_posts.* FROM user_posts")
    LiveData<List<UserPost>> getPosts();
}
