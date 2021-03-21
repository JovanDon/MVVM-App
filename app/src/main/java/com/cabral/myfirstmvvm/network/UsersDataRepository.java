package com.cabral.myfirstmvvm.network;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cabral.myfirstmvvm.models.UserDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersDataRepository {
    private static final String TAG = "UsersDataRepository";
    private static final UsersDataRepository ourInstance = new UsersDataRepository();;

    private MutableLiveData<List<UserDetails>> mutableLiveUserData= new MutableLiveData<>();

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


}
