package com.cabral.myfirstmvvm.network;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.cabral.myfirstmvvm.network.db.daos.UserAddressDao;
import com.cabral.myfirstmvvm.network.db.daos.UserCompanyDao;
import com.cabral.myfirstmvvm.network.db.entities.Address;
import com.cabral.myfirstmvvm.network.db.entities.User;
import com.cabral.myfirstmvvm.network.db.entities.UserCompany;
import com.cabral.myfirstmvvm.network.db.entities.UserPostEntity;
import com.cabral.myfirstmvvm.network.db.relations.UserDetailsRelation;
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
    private RoomDb dbInstance;
    private  final UserDao mUserDao;
    private  final UserCompanyDao mUserCompanyDao;
    private  final UserAddressDao mUserAddressDao;
    private  final UserPostDao mUserPostDao;

    private MutableLiveData<List<UserDetails>> mutableLiveUserData= new MutableLiveData<>();
    private MutableLiveData<UserDetails> mutableLiveUserDetails= new MutableLiveData<>();
    private MutableLiveData<List<UserPost>> mutableLiveUserPosts= new MutableLiveData<>();

    private UsersDataRepository(Context context) {
        dbInstance=RoomDb.getDatabase(context);
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

                   insertUserData(user1,address1,company1);
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

               LiveData<List<UserDetailsRelation>> userList=mUserDao.getUsers();

               LiveData<List<UserDetails>> mUsers = Transformations.switchMap(userList, myUserList -> changeUserRelationtoUserObject(myUserList) );

               return mUsers;
           }

           @NonNull
           @Override
           protected Call<List<UserDetails>> createCall() {
               Call<List<UserDetails>> call =  ApiClient.getInstance().getUsers();

               return call;
           }


       }.getAsLiveData();
    }


    private void insertUserData(User user1, Address address1, UserCompany company1) {
        RoomDb.insertUserData(dbInstance,user1,address1,company1);
    }

    public LiveData<Resource<UserDetails>> getUserDetails(int user_id) {

        return new NetworkBoundResource<UserDetails, UserDetails>() {

            @Override
            protected void saveCallResult(@NonNull UserDetails user) {

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

                    insertUserData(user1,address1,company1);

            }

            @Override
            protected boolean shouldFetch(@Nullable UserDetails data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<UserDetails> loadFromDb() {

                LiveData<UserDetailsRelation> user=mUserDao.getUser(user_id);

                LiveData<UserDetails> mUser = Transformations.switchMap(user, myUser -> changeUserRelationtoUserObject(myUser) );

                return mUser;
            }

            @NonNull
            @Override
            protected Call<UserDetails> createCall() {
                Call<UserDetails> call = ApiClient.getInstance().getUserDetails(user_id);
                return call;
            }


        }.getAsLiveData();
    }

    public LiveData<Resource<List<UserPost>>> getUserPosts(int user_id) {


        return new NetworkBoundResource<List<UserPost>, List<UserPost>>() {
            @Override
            protected void saveCallResult(@NonNull List<UserPost> itemList) {
                for (UserPost userPost: itemList) {

                    UserPostEntity userPost1=new UserPostEntity(
                            userPost.getId(),
                            user_id,
                            userPost.getTitle(),
                            userPost.getBody()
                    );

                    mUserPostDao.insert(userPost1);

                }
            }

            @NonNull
            @Override
            protected LiveData<List<UserPost>> loadFromDb() {
                LiveData<List<UserPostEntity>> userPosts=mUserPostDao.getPosts(user_id);

                LiveData<List<UserPost>> userPostResponse=Transformations.switchMap(userPosts,myPostList->changeUserPostEntitytoPostResponseObject(myPostList));

                return userPostResponse;
            }

            @Override
            protected boolean shouldFetch(@Nullable List<UserPost> data) {
                return true;
            }

            @NonNull
            @Override
            protected Call<List<UserPost>> createCall() {
                Call<List<UserPost>> call = ApiClient.getInstance().getPosts(user_id);
                return call;
            }
        }.getAsLiveData();

    }


    private LiveData<List<UserDetails>> changeUserRelationtoUserObject(List<UserDetailsRelation> myUserList) {
        MutableLiveData<List<UserDetails>> results= new MutableLiveData<List<UserDetails>>();
        List<UserDetails> userDetailsList= new ArrayList<>();
        for (UserDetailsRelation userDetails :myUserList) {
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
            userDetailsList.add(new UserDetails(
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
        results.setValue(userDetailsList);
        return  results;
    }

    private LiveData<UserDetails> changeUserRelationtoUserObject(UserDetailsRelation userDetails) {
        MutableLiveData<UserDetails> results= new MutableLiveData<UserDetails>();


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
        UserDetails userDetail =new UserDetails(
                userDetails.userWithAddress.user.getId(),
                userDetails.userWithAddress.user.getName(),
                userDetails.userWithAddress.user.getUsername(),
                userDetails.userWithAddress.user.getEmail(),
                userDetails.userWithAddress.user.getPhone(),
                userDetails.userWithAddress.user.getWebsite(),
                address,
                company
        );

        results.setValue(userDetail);
        return  results;
    }

    private LiveData<List<UserPost>> changeUserPostEntitytoPostResponseObject(List<UserPostEntity> myPostList) {
        MutableLiveData<List<UserPost>> results= new MutableLiveData<>();
        List<UserPost> userPostList= new ArrayList<>();

        for (UserPostEntity userPost :myPostList) {

            userPostList.add(new UserPost(
                    userPost.getId(),
                    userPost.getUserId(),
                    userPost.getTitle(),
                    userPost.getBody()
            ));
        }
        results.setValue(userPostList);
        return  results;
    }

}
