package com.cabral.myfirstmvvm.viewmodels;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cabral.myfirstmvvm.models.UserDetails;
import com.cabral.myfirstmvvm.network.UsersDataRepository;

import java.util.List;

public class UserListViewModel extends AndroidViewModel {

    private final UsersDataRepository mRepository;
    private final LiveData< List<UserDetails> > mUsers;

    public UserListViewModel(@NonNull Application application) {
        super(application);
        mRepository = UsersDataRepository.getInstance();

        mUsers = mRepository.getUserList();
    }

    public LiveData<List<UserDetails>> getUsers() {
        return mUsers;
    }
}