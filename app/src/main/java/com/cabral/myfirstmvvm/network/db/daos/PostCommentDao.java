package com.cabral.myfirstmvvm.network.db.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cabral.myfirstmvvm.network.db.entities.PostComment;

import java.util.List;

@Dao
public interface
PostCommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PostComment postComment);


    @Query("SELECT post_comment.* FROM post_comment WHERE post_comment.postId=:post_id")
    LiveData<List<PostComment>> getPostComments(int post_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PostComment> itemList);
}
