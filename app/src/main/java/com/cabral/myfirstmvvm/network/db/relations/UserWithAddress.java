package com.cabral.myfirstmvvm.network.db.relations;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Junction;
import androidx.room.Relation;

import com.cabral.myfirstmvvm.network.db.entities.Address;
import com.cabral.myfirstmvvm.network.db.entities.User;

public class UserWithAddress {
    @Embedded
    public User user;

    @Relation(
            parentColumn = "id",
            entityColumn = "user_id"
    )
    public Address address;
}
