package com.cabral.myfirstmvvm.network.db.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.cabral.myfirstmvvm.network.db.entities.UserPostEntity;

import java.util.List;

public class UserDetailsWithPosts {
    @Embedded
    public UserDetailsRelation userDetails;
    @Relation(
            parentColumn = "id",
            entityColumn = "userId"
    )
    public List<UserPostEntity> userPosts;

}
