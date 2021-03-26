package com.cabral.myfirstmvvm.network.db.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.cabral.myfirstmvvm.network.db.entities.UserPost;

import java.util.List;

public class UserDetailsWithPosts {
    @Embedded
    public List<UserPost> userPosts;
    @Relation(
            entity = UserWithAddress.class,
            entityColumn = "id",
            parentColumn = "userId"
    )
    public UserDetails userDetails;
}
