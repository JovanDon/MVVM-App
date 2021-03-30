package com.cabral.myfirstmvvm.ui.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cabral.myfirstmvvm.R;
import com.cabral.myfirstmvvm.databinding.CommentItemLayoutBinding;
import com.cabral.myfirstmvvm.network.db.entities.PostComment;

import java.util.ArrayList;
import java.util.List;

public class PostCommentsAdapter extends RecyclerView.Adapter<PostCommentsAdapter.PostCommentHolder> {

    List<? extends PostComment> mComments=new ArrayList<>();

    public PostCommentsAdapter() {
        setHasStableIds(true);
    }

    public void  setComments(List<? extends PostComment> mComments){
        if(mComments==null){
            this.mComments=mComments;
            notifyItemRangeInserted(0,mComments.size());
        }
        else{
            DiffUtil.DiffResult results=DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mComments.size();
                }

                @Override
                public int getNewListSize() {
                    return mComments.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mComments.get(newItemPosition).getId()==mComments.get(oldItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    PostComment oldComment=mComments.get(oldItemPosition);
                    PostComment newComment=mComments.get(newItemPosition);
                    return oldComment.getId() == newComment.getId()
                            && TextUtils.equals(newComment.getEmail(),newComment.getEmail())
                            && TextUtils.equals(newComment.getName(),newComment.getName())
                            && TextUtils.equals(newComment.getBody(),newComment.getBody());
                }
            });
            this.mComments=mComments;
            results.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public PostCommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommentItemLayoutBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.comment_item_layout, parent,false);
        return new PostCommentsAdapter.PostCommentHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostCommentHolder holder, int position) {
        holder.binding.setComment(mComments.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return this.mComments.size();
    }

    static class PostCommentHolder extends RecyclerView.ViewHolder {

        final CommentItemLayoutBinding binding;

        public PostCommentHolder(CommentItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
