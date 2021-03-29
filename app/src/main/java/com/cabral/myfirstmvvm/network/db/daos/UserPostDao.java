package com.cabral.myfirstmvvm.network.db.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cabral.myfirstmvvm.network.db.entities.UserPostEntity;

import java.util.List;

@Dao
public interface UserPostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserPostEntity UserPostEntity);


    @Query("SELECT user_posts.* FROM user_posts WHERE user_posts.userId=:user_id")
    LiveData<List<UserPostEntity>> getPosts(int user_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<UserPostEntity> postsList);
}
