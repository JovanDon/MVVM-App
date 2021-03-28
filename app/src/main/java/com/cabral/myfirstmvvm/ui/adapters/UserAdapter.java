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
import com.cabral.myfirstmvvm.databinding.UserItemLayoutBinding;
import com.cabral.myfirstmvvm.responses.UserDetails;
import com.cabral.myfirstmvvm.ui.callbacks.UserClickCallback;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    List<? extends UserDetails> mUserList;

    @Nullable
    private final UserClickCallback mUserClickCallback;

    public UserAdapter(@Nullable UserClickCallback clickCallback) {
        mUserClickCallback = clickCallback;
        setHasStableIds(true);
    }
    public void setUserList( List<? extends UserDetails> userList){
        if(mUserList==null){
            mUserList=userList;
            notifyItemRangeInserted(0,mUserList.size());
        }else{
            DiffUtil.DiffResult results=DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mUserList.size();
                }

                @Override
                public int getNewListSize() {
                    return userList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return userList.get(newItemPosition).getUser_id()==mUserList.get(oldItemPosition).getUser_id();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    UserDetails oldUser=mUserList.get(oldItemPosition);
                    UserDetails newUser=userList.get(newItemPosition);
                    return oldUser.getUser_id() == newUser.getUser_id()
                            && TextUtils.equals(oldUser.getName(),newUser.getName())
                            && TextUtils.equals(oldUser.getUsername(),newUser.getUsername())
                            && TextUtils.equals(oldUser.getEmail(),newUser.getEmail())
                            && TextUtils.equals(oldUser.getAddress().getCity(),newUser.getAddress().getCity())
                            && TextUtils.equals(oldUser.getAddress().getStreet(),newUser.getAddress().getStreet());
                }
            });
            mUserList=userList;
            results.dispatchUpdatesTo(this);
        }

    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserItemLayoutBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.user_item_layout, parent,false);
        binding.setCallback(mUserClickCallback);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.binding.setUserDetails(mUserList.get(position));
        holder.binding.executePendingBindings();
    }


    @Override
    public int getItemCount() {
        return mUserList==null? 0 : mUserList.size();
    }
    @Override
    public long getItemId(int position) {
        return mUserList.get(position).getUser_id();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        final UserItemLayoutBinding binding;

        public UserViewHolder(UserItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
