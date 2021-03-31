package com.cabral.myfirstmvvm.network;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.cabral.myfirstmvvm.network.db.daos.PostCommentDao;
import com.cabral.myfirstmvvm.network.db.entities.Address;
import com.cabral.myfirstmvvm.network.db.entities.PostComment;
import com.cabral.myfirstmvvm.network.db.entities.User;
import com.cabral.myfirstmvvm.network.db.entities.UserCompany;
import com.cabral.myfirstmvvm.network.db.entities.UserPostEntity;
import com.cabral.myfirstmvvm.network.db.relations.UserDetailsRelation;
import com.cabral.myfirstmvvm.responses.UserDetails;
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
    private  final UserPostDao mUserPostDao;
    private  final PostCommentDao mPostCommentDao;

    private MutableLiveData<List<UserDetails>> mutableLiveUserData= new MutableLiveData<>();
    private MutableLiveData<PostComment> mutableLivePostCommentData= new MutableLiveData<>();
    private MutableLiveData<String> mutableLivedeleteResponse= new MutableLiveData<>();

    private UsersDataRepository(Context context) {
        dbInstance=RoomDb.getDatabase(context);
        mUserDao= RoomDb.getDatabase(context).userDao();
        mUserPostDao= RoomDb.getDatabase(context).userPostDao();
        mPostCommentDao= RoomDb.getDatabase(context).postCommentDao();
    }

    public static UsersDataRepository getInstance(Context context){
        if(ourInstance==null){
            ourInstance= new UsersDataRepository(context);
        }
        return ourInstance;
    }

    public LiveData<List<UserDetails>> getUserList(int pagenumber){
        Call<List<UserDetails>> call =  ApiClient.getInstance().getUsers(0,pagenumber*10);
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

    public LiveData<Resource<List<UserDetails>>>getUsers(int pageNumber){
        if(pageNumber==0){
            pageNumber=1;
        }
        int finalPageNumber = pageNumber;
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

                   Call<List<UserPostEntity>> call = ApiClient.getInstance().getPosts(user.getUser_id(),0,10);
                   call.enqueue(new Callback<List<UserPostEntity>>() {
                       @Override
                       public void onResponse(Call<List<UserPostEntity>> call, Response<List<UserPostEntity>> response) {

                           (new MyTask(response.body())).execute();

                       }

                       @Override
                       public void onFailure(Call<List<UserPostEntity>> call, Throwable t) {
                          Log.e("Failure","Failed getting corresponding posts for User");

                       }
                   });
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
               Call<List<UserDetails>> call =  ApiClient.getInstance().getUsers((finalPageNumber -1)*10, finalPageNumber * 10);

               return call;
           }


       }.getAsLiveData();
    }


    private void insertUserData(User user1, Address address1, UserCompany company1) {
        RoomDb.insertUserData(dbInstance,user1,address1,company1);
    }

    public LiveData<Resource<List<UserPostEntity>>> getUserPosts(int user_id, int pageNumber) {
        if(pageNumber==0){
            pageNumber++;
        }


        int finalPageNumber = pageNumber;
        return new NetworkBoundResource<List<UserPostEntity>, List<UserPostEntity>>() {
            @Override
            protected void saveCallResult(@NonNull List<UserPostEntity> itemList) {
                mUserPostDao.insertAll( itemList);

                for (UserPostEntity postEntity:itemList) {//save all corespondingTasks
                    Call<List<PostComment>> call = ApiClient.getInstance().getPostComments(postEntity.getId());
                    call.enqueue(new Callback<List<PostComment>>() {
                        @Override
                        public void onResponse(Call<List<PostComment>> call, Response<List<PostComment>> response) {

                            (new CommentSaverTask(response.body())).execute();

                        }

                        @Override
                        public void onFailure(Call<List<PostComment>> call, Throwable t) {
                            Log.e("Failure","Failed getting corresponding posts for User");

                        }
                    });
                }


            }

            @NonNull
            @Override
            protected LiveData<List<UserPostEntity>> loadFromDb() {
                LiveData<List<UserPostEntity>> userPosts=mUserPostDao.getPosts(user_id);

                return userPosts;
            }

            @Override
            protected boolean shouldFetch(@Nullable List<UserPostEntity> data) {
                return true;
            }

            @NonNull
            @Override
            protected Call<List<UserPostEntity>> createCall() {
                Call<List<UserPostEntity>> call = ApiClient.getInstance().getPosts(user_id,(finalPageNumber -1)*10, finalPageNumber *10);
                return call;
            }
        }.getAsLiveData();

    }

    public LiveData<PostComment> submitPostComment(PostComment postComment){
        Call<PostComment> call = ApiClient.getInstance().submitPostComment(postComment);

        call.enqueue(new Callback<PostComment>() {
            @Override
            public void onResponse(Call<PostComment> call, Response<PostComment> response) {

                mutableLivePostCommentData.setValue( response.body());
            }

            @Override
            public void onFailure(Call<PostComment> call, Throwable t) {
                Log.d(TAG, "onFailure: failed to fetch user list from server"+t.getMessage());
            }
        });
        return mutableLivePostCommentData;
    }


    public LiveData<PostComment> updatePostComment(PostComment postComment){
        Call<PostComment> call = ApiClient.getInstance().updatePostComment(postComment.getId(),postComment);

        call.enqueue(new Callback<PostComment>() {
            @Override
            public void onResponse(Call<PostComment> call, Response<PostComment> response) {

                mutableLivePostCommentData.setValue( response.body());
            }

            @Override
            public void onFailure(Call<PostComment> call, Throwable t) {
                Log.d(TAG, "onFailure: failed to fetch user list from server"+t.getMessage());
            }
        });
        return mutableLivePostCommentData;
    }//deleteComment

    public LiveData<String> deletePostComment(PostComment postComment){
        Call<Object> call = ApiClient.getInstance().deleteComment(postComment.getId());

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                mutableLivedeleteResponse.setValue( "Success");

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d(TAG, "onFailure: failed to fetch user list from server"+t.getMessage());
                mutableLivedeleteResponse.setValue( "Failure");
            }
        });

        return mutableLivedeleteResponse;
    }

    public LiveData<Resource<List<PostComment>>> getPostComment(int post_id) {


        return new NetworkBoundResource<List<PostComment>, List<PostComment>>() {
            @Override
            protected void saveCallResult(@NonNull List<PostComment> itemList) {
                mPostCommentDao.insertAll( itemList);
            }

            @NonNull
            @Override
            protected LiveData<List<PostComment>> loadFromDb() {
                LiveData<List<PostComment>> postComments=mPostCommentDao.getPostComments(post_id);

                return postComments;
            }

            @Override
            protected boolean shouldFetch(@Nullable List<PostComment> data) {
                return true;
            }

            @NonNull
            @Override
            protected Call<List<PostComment>> createCall() {
                Call<List<PostComment>> call = ApiClient.getInstance().getPostComments(post_id);
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


    private class MyTask extends AsyncTask<String, Void, Void> {
        List<UserPostEntity> postList;

        public MyTask(List<UserPostEntity> postList) {
            this.postList=postList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            mUserPostDao.insertAll( this.postList );
            return  null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    private class CommentSaverTask extends AsyncTask<String, Void, Void> {
        List<PostComment> commentList;

        public CommentSaverTask(List<PostComment> comments) {
            this.commentList=comments;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            mPostCommentDao.insertAll( this.commentList );
            return  null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

}
