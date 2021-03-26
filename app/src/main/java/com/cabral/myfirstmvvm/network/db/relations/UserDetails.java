package com.cabral.myfirstmvvm.network.db.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.cabral.myfirstmvvm.network.db.entities.User;
import com.cabral.myfirstmvvm.network.db.entities.UserCompany;

public class UserDetails {
    @Embedded
    public UserCompany company;
    @Relation(
            parentColumn = "user_id",
            entityColumn = "id",
            entity = User.class
    )
    public UserWithAddress userWithAddress;

}
