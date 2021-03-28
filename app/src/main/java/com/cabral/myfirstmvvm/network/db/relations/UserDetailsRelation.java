package com.cabral.myfirstmvvm.network.db.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.cabral.myfirstmvvm.network.db.entities.User;
import com.cabral.myfirstmvvm.network.db.entities.UserCompany;

public class UserDetailsRelation {
    @Embedded
    public UserWithAddress userWithAddress;
    @Relation(
            parentColumn = "id",
            entityColumn = "user_id"
    )
    public UserCompany company;

}
