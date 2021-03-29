package com.cabral.myfirstmvvm.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cabral.myfirstmvvm.responses.UserDetails;
import com.cabral.myfirstmvvm.responses.UserPost;
import com.cabral.myfirstmvvm.network.UsersDataRepository;
import com.cabral.myfirstmvvm.util.Resource;

import java.util.List;

import static com.cabral.myfirstmvvm.viewmodels.UserListViewModel.QUERY_EXHAUSTED;

public class UserViewModel extends AndroidViewModel {

    private static final String TAG = "UserViewModel";

    private final UsersDataRepository mRepository;
    private final MediatorLiveData<Resource<UserDetails>> mUser=new MediatorLiveData<>();
    private final MediatorLiveData<Resource<List<UserPost>>> mUserPost=new MediatorLiveData<>();
    private int user_id;

    // query extras
    private boolean isQueryExhausted;
    private boolean isPerformingQuery;
    private boolean cancelRequest;
    private long requestStartTime;
    private int pageNumber;

    public UserViewModel(@NonNull Application application,int user_id) {
        super(application);
        mRepository = UsersDataRepository.getInstance(application.getApplicationContext());
        this.user_id=user_id;

        getUserPostData(0);
    }

    public LiveData<UserDetails> getUserDetails() {
        LiveData<UserDetails> mUserDetail=Transformations.switchMap(mUser,myUerDetail->changeUserDetail(myUerDetail));
        return mUserDetail;
    }

    private LiveData<UserDetails> changeUserDetail(Resource<UserDetails> myUerDetail) {
        MutableLiveData<UserDetails> userDetailsMutableLiveData=new MutableLiveData<>();
        userDetailsMutableLiveData.setValue(myUerDetail.data);
        return userDetailsMutableLiveData;
    }

    public LiveData<Resource<List<UserPost>>> getmUserPost() {
        return mUserPost;
    }

    private void executeFetchUserDetails(){
        requestStartTime = System.currentTimeMillis();
        cancelRequest = false;
        isPerformingQuery = true;
        final LiveData<Resource<UserDetails>> repositorySource=mRepository.getUserDetails(user_id);

        mUser.addSource(repositorySource, new Observer<Resource<UserDetails>>() {
            @Override
            public void onChanged(Resource<UserDetails> userDetailsResource) {

                if(!cancelRequest){
                    if(userDetailsResource!=null){

                        if(userDetailsResource.status == Resource.Status.SUCCESS){
                            Log.d(TAG, "onChanged: REQUEST TIME: " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds.");
                            Log.d(TAG, "onChanged: page number: " + pageNumber);
                            Log.d(TAG, "onChanged: " + userDetailsResource.data);
                            isPerformingQuery=false;
                            if(userDetailsResource.data!=null){
                                mUser.setValue( new Resource<>(
                                        Resource.Status.SUCCESS,
                                        userDetailsResource.data,
                                        QUERY_EXHAUSTED
                                ));
                            }
                            mUser.removeSource(repositorySource);
                        }
                        else if(userDetailsResource.status== Resource.Status.ERROR) {
                            Log.d(TAG, "onChanged: REQUEST TIME: " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds.");
                            isPerformingQuery = false;
                            if (userDetailsResource.message.equals(QUERY_EXHAUSTED)) {
                                isQueryExhausted = true;
                            }
                            mUser.removeSource(repositorySource);
                        }
                        mUser.setValue(userDetailsResource);
                    }
                    else {
                        mUser.removeSource(repositorySource);
                    }
                }else{
                    mUser.removeSource(repositorySource);
                }

            }
        });
    }

    public void getUserPostData( int pageNumber){
        if(!isPerformingQuery){
            if(pageNumber == 0){
                pageNumber = 1;
            }
            this.pageNumber = pageNumber;
            isQueryExhausted = false;
            executeFetchUserPosts();
            //executeFetchUserDetails();
        }
    }

    public void getUsersNextPage(){
        if(!isQueryExhausted && !isPerformingQuery){
            pageNumber++;
            executeFetchUserPosts();
        }
    }



    private void executeFetchUserPosts(){
        requestStartTime = System.currentTimeMillis();
        cancelRequest = false;
        isPerformingQuery = true;
        final LiveData<Resource<List<UserPost>>> repositorySource=mRepository.getUserPosts(user_id);

        mUserPost.addSource(repositorySource, new Observer<Resource<List<UserPost>>>() {
            @Override
            public void onChanged(Resource<List<UserPost>> listResource) {

                if(!cancelRequest){
                    if(listResource!=null){

                        if(listResource.status == Resource.Status.SUCCESS){
                            Log.d(TAG, "onChanged: REQUEST TIME: " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds.");
                            Log.d(TAG, "onChanged: page number: " + pageNumber);
                            Log.d(TAG, "onChanged: " + listResource.data);
                            isPerformingQuery=false;
                            if(listResource.data!=null){
                                mUserPost.setValue( new Resource<>(
                                        Resource.Status.SUCCESS,
                                        listResource.data,
                                        QUERY_EXHAUSTED
                                ));
                            }
                            mUserPost.removeSource(repositorySource);
                        }
                        else if(listResource.status== Resource.Status.ERROR) {
                            Log.d(TAG, "onChanged: REQUEST TIME: " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds.");
                            isPerformingQuery = false;
                            if (listResource.message.equals(QUERY_EXHAUSTED)) {
                                isQueryExhausted = true;
                            }
                            mUserPost.removeSource(repositorySource);
                        }
                        mUserPost.setValue(listResource);
                    }
                    else {
                        mUserPost.removeSource(repositorySource);
                    }
                }else{
                    mUserPost.removeSource(repositorySource);
                }

            }
        });
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

        private final UserDetails mUser;

        public Factory(@NonNull Application application, UserDetails user) {
            mApplication = application;
            mUser = user;
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new UserViewModel(mApplication, mUser.getUser_id());
        }

    }
}
