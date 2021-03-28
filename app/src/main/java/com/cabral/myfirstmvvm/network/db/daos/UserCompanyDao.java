package com.cabral.myfirstmvvm.network.db.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.cabral.myfirstmvvm.network.db.entities.UserCompany;

@Dao
public interface UserCompanyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserCompany(UserCompany company);

}
