package com.cabral.myfirstmvvm.network;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cabral.myfirstmvvm.AppExecutors;
import com.cabral.myfirstmvvm.network.db.daos.UserAddressDao;
import com.cabral.myfirstmvvm.network.db.daos.UserCompanyDao;
import com.cabral.myfirstmvvm.network.db.entities.Address;
import com.cabral.myfirstmvvm.network.db.entities.User;
import com.cabral.myfirstmvvm.network.db.entities.UserCompany;
import com.cabral.myfirstmvvm.network.db.relations.UserDetailsRelation;
import com.cabral.myfirstmvvm.responses.ApiResponse;
import com.cabral.myfirstmvvm.responses.UserDetails;
import com.cabral.myfirstmvvm.responses.UserPost;
import com.cabral.myfirstmvvm.network.db.RoomDb;
import com.cabral.myfirstmvvm.network.db.daos.UserDao;
import com.cabral.myfirstmvvm.network.db.daos.UserPostDao;
import com.cabral.myfirstmvvm.util.NetworkBoundResource;
import com.cabral.myfirstmvvm.util.Resource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersDataRepository {
    private static final String TAG = "UsersDataRepository";
    private static UsersDataRepository ourInstance;
    private  final UserDao mUserDao;
    private  final UserCompanyDao mUserCompanyDao;
    private  final UserAddressDao mUserAddressDao;
    private  final UserPostDao mUserPostDao;

    private MutableLiveData<List<UserDetails>> mutableLiveUserData= new MutableLiveData<>();
    private MutableLiveData<UserDetails> mutableLiveUserDetails= new MutableLiveData<>();
    private MutableLiveData<List<UserPost>> mutableLiveUserPosts= new MutableLiveData<>();

    private UsersDataRepository(Context context) {
        mUserDao= RoomDb.getDatabase(context).userDao();
        mUserAddressDao=RoomDb.getDatabase(context).userAddressDao();
        mUserCompanyDao=RoomDb.getDatabase(context).userCompanyDao();
        mUserPostDao= RoomDb.getDatabase(context).userPostDao();
    }

    public static UsersDataRepository getInstance(Context context){
        if(ourInstance==null){
            ourInstance= new UsersDataRepository(context);
        }
        return ourInstance;
    }

    public LiveData<List<UserDetails>> getUserList(){
        Call<List<UserDetails>> call =  ApiClient.getInstance().getUsers();
        call.enqueue(new Callback<List<UserDetails>>() {
            @Override
            public void onResponse(Call<List<UserDetails>> call, Response<List<UserDetails>> response) {

                mutableLiveUserData.setValue( response.body());
            }

            @Override
            public void onFailure(Call<List<UserDetails>> call, Throwable t) {
                Log.d(TAG, "onFailure: failed to fetch user list from server"+t.getMessage());
            }
        });
        return mutableLiveUserData;
    }

    public LiveData<Resource<List<UserDetails>>>getUsers(){
       return new NetworkBoundResource<List<UserDetails>, List<UserDetails>>() {

           @Override
           protected void saveCallResult(@NonNull List<UserDetails> itemList) {
               for (UserDetails user : itemList) {
                   User user1= new User(user.getUser_id(),
                           user.getName(),
                           user.getUsername(),
                           user.getEmail(),
                           user.getPhone(),
                           user.getWebsite());
                   Address address1=new Address(user.getUser_id(),
                           user.getAddress().getStreet(),
                           user.getAddress().getSuite(),
                           user.getAddress().getCity(),
                           user.getAddress().getZipcode(),
                           user.getAddress().getGeo().getLatitude(),
                           user.getAddress().getGeo().getLongitude()
                           );
                   UserCompany company1=new UserCompany(
                           user.getUser_id(),
                           user.getCompany().getName(),
                           user.getCompany().getCatchPhrase(),
                           user.getCompany().getBs()
                   );

                   mUserDao.insertUser(user1);
                   mUserAddressDao.insertUserAddress(address1);
                   mUserCompanyDao.insertUserCompany(company1);
                   //mUserDao.insertUser(user);
               }
           }

           @Override
           protected boolean shouldFetch(@Nullable List<UserDetails> data) {
               return true;
           }

           @NonNull
           @Override
           protected LiveData<List<UserDetails>> loadFromDb() {
               MutableLiveData<List<UserDetails>> mUser= new MutableLiveData<List<UserDetails>>();
               List<UserDetails> results=new ArrayList<>();
               LiveData<List<UserDetailsRelation>> userList=mUserDao.getUsers();

               if(userList.getValue()!=null)
               for (UserDetailsRelation userDetails :userList.getValue()) {
                   UserDetails.LatLng latLng= new UserDetails.LatLng(
                           userDetails.userWithAddress.address.getLat(),
                           userDetails.userWithAddress.address.getLng()
                   );
                   UserDetails.UserCompany company =new UserDetails.UserCompany(
                           userDetails.company.getName(),
                           userDetails.company.getCatchPhrase(),
                           userDetails.company.getBs()
                   );

                   UserDetails.UserAddress address=new UserDetails.UserAddress(
                           userDetails.userWithAddress.address.getStreet(),
                           userDetails.userWithAddress.address.getSuite(),
                           userDetails.userWithAddress.address.getCity(),
                           userDetails.userWithAddress.address.getZipcode(),
                           latLng
                   );
                   results.add(new UserDetails(
                           userDetails.userWithAddress.user.getId(),
                           userDetails.userWithAddress.user.getName(),
                           userDetails.userWithAddress.user.getUsername(),
                           userDetails.userWithAddress.user.getEmail(),
                           userDetails.userWithAddress.user.getPhone(),
                           userDetails.userWithAddress.user.getWebsite(),
                           address,
                           company
                           ));
               }

               mUser.setValue(results);
               return mUser;
           }

           @NonNull
           @Override
           protected Call<List<UserDetails>> createCall() {
               Call<List<UserDetails>> call =  ApiClient.getInstance().getUsers();

               return call;
           }


       }.getAsLiveData();
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
