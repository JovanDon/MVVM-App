package com.cabral.myfirstmvvm.ui.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cabral.myfirstmvvm.R;
import com.cabral.myfirstmvvm.databinding.UserPostItemBinding;
import com.cabral.myfirstmvvm.responses.UserPost;
import com.cabral.myfirstmvvm.ui.callbacks.PostClickCallback;

import java.util.List;

public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.UserPostHolder> {

    private final PostClickCallback mPostClickCallback;
    List<? extends UserPost> mUserPosts;

    public UserPostAdapter(@Nullable PostClickCallback mPostClickCallback) {
        this.mPostClickCallback = mPostClickCallback;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public UserPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserPostItemBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.user_post_item, parent,false);
        binding.setCallback(mPostClickCallback);
        return new UserPostAdapter.UserPostHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserPostHolder holder, int position) {

        holder.binding.setUserPost(mUserPosts.get(position));
        holder.binding.executePendingBindings();
    }
    public void setPostList( List<? extends UserPost> postList){
        if(postList==null){
            mUserPosts=postList;
            notifyItemRangeInserted(0,mUserPosts.size());
        }

    }

    

    @Override
    public int getItemCount() {
        return mUserPosts==null? 0 : mUserPosts.size();
    }

    static class UserPostHolder extends RecyclerView.ViewHolder {

        final UserPostItemBinding binding;

        public UserPostHolder(UserPostItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
