package com.cabral.myfirstmvvm.network;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cabral.myfirstmvvm.responses.UserDetails;
import com.cabral.myfirstmvvm.responses.UserPost;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class UsersDataRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MutableLiveData<List<UserDetails>> mutableLiveUserData= new MutableLiveData<>();
    private MutableLiveData<UserDetails> mutableLiveUserDetails= new MutableLiveData<>();
    private MutableLiveData<List<UserPost>> mutableLiveUserPosts= new MutableLiveData<>();

   @Before
   public void init(){

   }
}