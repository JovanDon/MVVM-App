package com.cabral.myfirstmvvm.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.cabral.myfirstmvvm.responses.ApiResponse;
import com.cabral.myfirstmvvm.responses.UserDetails;
import com.cabral.myfirstmvvm.network.UsersDataRepository;
import com.cabral.myfirstmvvm.util.Resource;

import java.util.List;

public class UserListViewModel extends AndroidViewModel {
    private static final String TAG = "UserListViewModel";
    public static final String QUERY_EXHAUSTED = "No more results.";

    private final UsersDataRepository mRepository;
    private final LiveData<List<UserDetails>> mUsers;
    private final MediatorLiveData<Resource<List<UserDetails>> > users=new MediatorLiveData<>();
    Context context;

    // query extras
    private boolean isQueryExhausted;
    private boolean isPerformingQuery;
    private boolean cancelRequest;
    private long requestStartTime;
    private int pageNumber;

    public UserListViewModel(@NonNull Application application) {
        super(application);
        context=application.getApplicationContext();
        mRepository = UsersDataRepository.getInstance(application.getApplicationContext());
        mUsers = mRepository.getUserList();
        getUsersApi(0);
    }

    public LiveData<List<UserDetails>> getUsers() {
        return mUsers;
    }
    public LiveData<Resource<List<UserDetails>>> getCachedUser() {
        return users;
    }

    public int getPageNumber(){
        return pageNumber;
    }

    public void getUsersApi( int pageNumber){
        if(!isPerformingQuery){
            if(pageNumber == 0){
                pageNumber = 1;
            }
            this.pageNumber = pageNumber;
            isQueryExhausted = false;
            executeGetUsers();
        }
    }

    public void getUsersNextPage(){
        if(!isQueryExhausted && !isPerformingQuery){
            pageNumber++;
            executeGetUsers();
        }
    }

    private void executeGetUsers() {
        requestStartTime = System.currentTimeMillis();
        cancelRequest = false;
        isPerformingQuery = true;
        final LiveData<Resource<List<UserDetails>>> repositorySource=mRepository.getUsers();

        users.addSource(repositorySource, new Observer<Resource<List<UserDetails>>>() {
            @Override
            public void onChanged(Resource<List<UserDetails>> listResource) {
                if(!cancelRequest){
                  if(listResource!=null){

                      if(listResource.status == Resource.Status.SUCCESS){
                          Log.d(TAG, "onChanged: REQUEST TIME: " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds.");
                          Log.d(TAG, "onChanged: page number: " + pageNumber);
                          Log.d(TAG, "onChanged: " + listResource.data);
                          isPerformingQuery=false;
                          if(listResource.data!=null){
                            users.setValue( new Resource<>(
                                    Resource.Status.SUCCESS,
                                    listResource.data,
                                    QUERY_EXHAUSTED
                                    ));
                          }
                          users.removeSource(repositorySource);
                      }
                      else if(listResource.status== Resource.Status.ERROR) {
                          Log.d(TAG, "onChanged: REQUEST TIME: " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds.");
                          isPerformingQuery = false;
                          if (listResource.message.equals(QUERY_EXHAUSTED)) {
                              isQueryExhausted = true;
                          }
                          users.removeSource(repositorySource);
                      }
                      users.setValue(listResource);
                  }else {
                      users.removeSource(repositorySource);
                  }
                }else{
                    users.removeSource(repositorySource);
                }
            }
        });
    }
}