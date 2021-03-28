package com.cabral.myfirstmvvm.network.db.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.cabral.myfirstmvvm.network.db.entities.Address;

@Dao
public interface UserAddressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserAddress(Address address);
}
