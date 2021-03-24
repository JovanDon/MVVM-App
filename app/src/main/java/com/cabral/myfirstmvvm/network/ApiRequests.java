package com.cabral.myfirstmvvm.network;

import com.cabral.myfirstmvvm.models.PostComments;
import com.cabral.myfirstmvvm.models.UserAlbum;
import com.cabral.myfirstmvvm.models.UserDetails;
import com.cabral.myfirstmvvm.models.UserPost;
import com.cabral.myfirstmvvm.models.UserTodo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiRequests {

    // //get users
    @GET("users")
    Call<List<UserDetails>> getUsers();

    // //get userPosts
    @GET("posts")
    Call<List<UserPost>> getPosts(@Query("userId") int userId);

    // //get users Details
    @GET("users")
    Call<List<UserDetails>> getUserDetails(@Query("id") int id);

    //get User albums
    @GET("albums")
    Call<List<UserAlbum>> getAlbums(@Query("userId") String userId);

    //get User todo
    @GET("albums")
    Call<List<UserTodo>> getTodo(@Query("userId") String userId);

    //get User todo
    @GET("comments")
    Call<List<PostComments>> getPostComments(@Query("postId") String postId);
}
