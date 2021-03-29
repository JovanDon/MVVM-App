package com.cabral.myfirstmvvm.network;

import com.cabral.myfirstmvvm.network.db.entities.UserPostEntity;
import com.cabral.myfirstmvvm.responses.PostComments;
import com.cabral.myfirstmvvm.responses.UserDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiRequests {

    // //get users
    @GET("users")
    Call<List<UserDetails>> getUsers();

    // //get userPosts
    @GET("posts")
    Call<List<UserPostEntity>> getPosts(@Query("userId") int userId);

    // //get users Details
    @GET("users/{id}")
    Call<UserDetails> getUserDetails(@Path("id") int id);


    //get User todo
    @GET("comments")
    Call<List<PostComments>> getPostComments(@Query("postId") String postId);

    //implement the following http verbs

    // POST
    // PUT/PATCH
    // DELETE
    // GET
}
