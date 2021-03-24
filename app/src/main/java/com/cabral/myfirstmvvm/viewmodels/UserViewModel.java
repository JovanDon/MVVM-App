package com.cabral.myfirstmvvm.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cabral.myfirstmvvm.models.UserDetails;
import com.cabral.myfirstmvvm.models.UserPost;
import com.cabral.myfirstmvvm.network.UsersDataRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private final UsersDataRepository mRepository;
    private final LiveData<UserDetails> mUser;
    private final LiveData< List<UserPost>> mUserPost;

    public UserViewModel(@NonNull Application application,int user_id) {
        super(application);
        mRepository = UsersDataRepository.getInstance();
        mUser = mRepository.getUserDetails(user_id);
        mUserPost = mRepository.getUserPosts(user_id);
    }

    public LiveData<UserDetails> getUserDetails() {
        return mUser;
    }

    public LiveData<List<UserPost>> getmUserPost() {
        return mUserPost;
    }


    /**
     * A creator is used to inject the user ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the User ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mUserId;

        public Factory(@NonNull Application application, int userId) {
            mApplication = application;
            mUserId = userId;
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new UserViewModel(mApplication, mUserId);
        }

    }
}
