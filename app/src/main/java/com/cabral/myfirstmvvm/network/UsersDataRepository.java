package com.cabral.myfirstmvvm.network;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cabral.myfirstmvvm.models.UserDetails;
import com.cabral.myfirstmvvm.models.UserPost;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersDataRepository {
    private static final String TAG = "UsersDataRepository";
    private static final UsersDataRepository ourInstance = new UsersDataRepository();;

    private MutableLiveData<List<UserDetails>> mutableLiveUserData= new MutableLiveData<>();
    private MutableLiveData<UserDetails> mutableLiveUserDetails= new MutableLiveData<>();
    private MutableLiveData<List<UserPost>> mutableLiveUserPosts= new MutableLiveData<>();

    public static UsersDataRepository getInstance(){
        return ourInstance;
    }

    public LiveData<List<UserDetails>> getUserList(){
        Call<List<UserDetails>> call = ApiClient.getInstance().getUsers();
        call.enqueue(new Callback<List<UserDetails>>() {
            @Override
            public void onResponse(Call<List<UserDetails>> call, Response<List<UserDetails>> response) {
                mutableLiveUserData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<UserDetails>> call, Throwable t) {
                Log.d(TAG, "onFailure: failed to fetch user list from server");
            }
        });
        return mutableLiveUserData;
    }


    public LiveData< UserDetails > getUserDetails(int user_id) {
        Call<List<UserDetails>> call = ApiClient.getInstance().getUserDetails(user_id);
        call.enqueue(new Callback<List<UserDetails>>() {
            @Override
            public void onResponse(Call<List<UserDetails>> call, Response<List<UserDetails>> response) {
                if(response.body().size()>0)
                mutableLiveUserDetails.setValue(response.body().get(0));
                else
                    mutableLiveUserDetails.setValue(null);
            }

            @Override
            public void onFailure(Call<List<UserDetails>> call, Throwable t) {
                Log.d(TAG, "onFailure: failed to fetch user Details list from server");
            }
        });
        return mutableLiveUserDetails;
    }

    public LiveData<List<UserPost>> getUserPosts(int user_id) {
        Call<List<UserPost>> call = ApiClient.getInstance().getPosts(user_id);
        call.enqueue(new Callback<List<UserPost>>() {
            @Override
            public void onResponse(Call<List<UserPost>> call, Response<List<UserPost>> response) {
                mutableLiveUserPosts.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<UserPost>> call, Throwable t) {
                Log.d(TAG, "onFailure: failed to fetch user Posts from server");
            }
        });
        return mutableLiveUserPosts;
    }
}
