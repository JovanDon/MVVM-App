package com.cabral.myfirstmvvm.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cabral.myfirstmvvm.R;
import com.cabral.myfirstmvvm.databinding.CommentItemLayoutBinding;
import com.cabral.myfirstmvvm.network.db.entities.PostComment;
import com.cabral.myfirstmvvm.ui.fragments.CommentDetailsFragment;
import com.cabral.myfirstmvvm.util.DialogLoader;
import com.cabral.myfirstmvvm.viewmodels.PostCommentViewModel;

import java.util.ArrayList;
import java.util.List;

public class PostCommentsAdapter extends RecyclerView.Adapter<PostCommentsAdapter.PostCommentHolder> {

    List<? extends PostComment> mComments;
    Context context;
    LifecycleOwner viewLifecycleOwner;

    public PostCommentsAdapter(Context context, LifecycleOwner viewLifecycleOwner) {
        this.context=context;
        this.viewLifecycleOwner=viewLifecycleOwner;
        setHasStableIds(true);
    }

    public void  setComments(List<? extends PostComment> commentList){
        if(mComments==null){
            this.mComments=commentList;
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



        holder.binding.editComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentDetailsFragment.newCommentName.setText(mComments.get(position).getName());
                CommentDetailsFragment.newComment.setText(mComments.get(position).getBody());
                CommentDetailsFragment.updateMode=true;
                CommentDetailsFragment.commentToUpdate=mComments.get(position);


            }
        });
        holder.binding.deleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteComment(mComments.get(position));
            }
        });
        holder.binding.executePendingBindings();
    }


    private void deleteComment(PostComment postComment) {
        DialogLoader dialogLoader=new DialogLoader(context);
        dialogLoader.showProgressDialog();

        LiveData<String> liveData=CommentDetailsFragment.viewModel.deletePostComment(
                postComment
        );

        liveData.observe(viewLifecycleOwner, result -> {
            dialogLoader.hideProgressDialog();
            if (result != null && result.equalsIgnoreCase("Success") ) {
                Toast.makeText(context, "Comment Deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }


        });
    }

    @Override
    public int getItemCount() {
        return mComments==null? 0 : mComments.size();
    }

    static class PostCommentHolder extends RecyclerView.ViewHolder {

        final CommentItemLayoutBinding binding;

        public PostCommentHolder(CommentItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
