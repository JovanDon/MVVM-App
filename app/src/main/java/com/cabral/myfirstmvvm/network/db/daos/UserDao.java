package com.cabral.myfirstmvvm.network.db.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.cabral.myfirstmvvm.network.db.entities.User;
import com.cabral.myfirstmvvm.network.db.relations.UserDetailsRelation;

import com.cabral.myfirstmvvm.network.db.relations.UserDetailsWithPosts;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Transaction
    @Query("SELECT User.* FROM User")
    LiveData<List<UserDetailsRelation>> getUsers();

    @Query("SELECT * FROM User where id = :user_id")
    LiveData<User> getUser(int user_id);

    @Transaction
    @Query("SELECT User.* FROM User WHERE User.id= :user_id")
    LiveData<UserDetailsWithPosts> getUserPostData(int user_id);
}
