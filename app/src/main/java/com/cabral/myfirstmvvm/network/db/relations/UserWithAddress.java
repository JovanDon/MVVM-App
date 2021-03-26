package com.cabral.myfirstmvvm.network.db.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.cabral.myfirstmvvm.network.db.entities.User;
import com.cabral.myfirstmvvm.network.db.entities.UserAddress;

public class UserWithAddress {
    @Embedded
    public UserAddress address;
    @Relation(
            parentColumn = "user_id",
            entityColumn = "id"
    )
    public User user;
}
