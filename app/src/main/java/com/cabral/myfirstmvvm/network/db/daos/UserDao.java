package com.cabral.myfirstmvvm.network.db.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.cabral.myfirstmvvm.models.UserDetails;
import com.cabral.myfirstmvvm.network.db.relations.UserDetailsWithPosts;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    void insertUser(UserDetails user);

    @Transaction
    @Query("SELECT User.* FROM User")
    LiveData<List<UserDetails>> getUsers();


    @Transaction
    @Query("SELECT User.* FROM User WHERE User.id= :user_id")
    LiveData<UserDetailsWithPosts> getUserPostData(int user_id);
}
