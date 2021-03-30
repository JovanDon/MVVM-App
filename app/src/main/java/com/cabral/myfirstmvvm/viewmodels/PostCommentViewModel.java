package com.cabral.myfirstmvvm.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cabral.myfirstmvvm.network.UsersDataRepository;
import com.cabral.myfirstmvvm.network.db.entities.PostComment;
import com.cabral.myfirstmvvm.responses.UserDetails;
import com.cabral.myfirstmvvm.util.Resource;

import java.util.List;

import static com.cabral.myfirstmvvm.viewmodels.UserListViewModel.QUERY_EXHAUSTED;

public class PostCommentViewModel extends AndroidViewModel {
    private static final String TAG = "PostCommentViewModel";

    private final UsersDataRepository mRepository;
    private final MediatorLiveData<Resource<List<PostComment>>> postComments=new MediatorLiveData<>();
    private final int post_id;

    // query extras
    private boolean isQueryExhausted;
    private boolean isPerformingQuery;
    private boolean cancelRequest;
    private long requestStartTime;
    private int pageNumber;


    public PostCommentViewModel(@NonNull Application application, int post_id) {
        super(application);

        mRepository = UsersDataRepository.getInstance(application.getApplicationContext());
        this.post_id=post_id;
        getUserCommentData(0);
    }

    public void getUserCommentData( int pageNumber){
        if(!isPerformingQuery){
            if(pageNumber == 0){
                pageNumber = 1;
            }
            this.pageNumber = pageNumber;
            isQueryExhausted = false;
            executeFetchComments();
        }
    }

    public void getUsersNextPage(){
        if(!isQueryExhausted && !isPerformingQuery){
            pageNumber++;
            executeFetchComments();
        }
    }
    private void executeFetchComments(){
        requestStartTime = System.currentTimeMillis();
        cancelRequest = false;
        isPerformingQuery = true;
        final LiveData<Resource<List<PostComment>>> repositorySource=mRepository.getPostComment(post_id);

        postComments.addSource(repositorySource, new Observer<Resource<List<PostComment>>>() {
            @Override
            public void onChanged(Resource<List<PostComment>> listResource) {

                if(!cancelRequest){
                    if(listResource!=null){

                        if(listResource.status == Resource.Status.SUCCESS){
                            Log.d(TAG, "onChanged: REQUEST TIME: " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds.");
                            Log.d(TAG, "onChanged: page number: " + pageNumber);
                            Log.d(TAG, "onChanged: " + listResource.data);
                            isPerformingQuery=false;
                            if(listResource.data!=null){
                                postComments.setValue( new Resource<>(
                                        Resource.Status.SUCCESS,
                                        listResource.data,
                                        QUERY_EXHAUSTED
                                ));
                            }
                            postComments.removeSource(repositorySource);
                        }
                        else if(listResource.status== Resource.Status.ERROR) {
                            Log.d(TAG, "onChanged: REQUEST TIME: " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds.");
                            isPerformingQuery = false;
                            if (listResource.message.equals(QUERY_EXHAUSTED)) {
                                isQueryExhausted = true;
                            }
                            postComments.removeSource(repositorySource);
                        }
                        postComments.setValue(listResource);
                    }
                    else {
                        postComments.removeSource(repositorySource);
                    }
                }else{
                    postComments.removeSource(repositorySource);
                }

            }
        });
    }

    public LiveData<PostComment>  submitComment(PostComment postComment){
        return mRepository.submitPostComment(postComment);
    }

    public LiveData<Resource<List<PostComment>>> getComments() {
        return postComments;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int post_id;

        public Factory(@NonNull Application application, int post_id) {
            mApplication = application;
            this.post_id = post_id;
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new PostCommentViewModel(mApplication, post_id);
        }

    }
}
