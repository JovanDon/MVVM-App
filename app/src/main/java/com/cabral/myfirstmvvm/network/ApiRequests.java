package com.cabral.myfirstmvvm.network;

import com.cabral.myfirstmvvm.network.db.entities.PostComment;
import com.cabral.myfirstmvvm.network.db.entities.UserPostEntity;
import com.cabral.myfirstmvvm.responses.UserDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiRequests {

    // //get users
    @GET("users")
    Call<List<UserDetails>> getUsers(@Query("_start") int _start, @Query("_end") int _end);

    // //get userPosts
    @GET("posts")
    Call<List<UserPostEntity>> getPosts(@Query("userId") int userId, @Query("_start") int _start, @Query("_end") int _end);

    // //get users Details
    @GET("users/{id}")
    Call<UserDetails> getUserDetails(@Path("id") int id);


    //get User todo
    @GET("comments")
    Call<List<PostComment>> getPostComments(@Query("postId") int postId);

    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("comments")
    Call<PostComment> submitPostComment(
            @Body PostComment postComment
    );

    @Headers("Content-Type: application/json; charset=UTF-8")
    @PUT("comments/{id}")
    Call<PostComment> updatePostComment(
            @Path("id") int id, @Body PostComment postComment
    );

    @Headers("Content-Type: application/json; charset=UTF-8")
    @DELETE("comments")
    Call<Object> deleteComment(
            @Query("id") int id
    );
}
