package com.cabral.myfirstmvvm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.cabral.myfirstmvvm.network.db.entities.UserPostEntity;
import com.cabral.myfirstmvvm.responses.UserDetails;

public class MainActivity extends AppCompatActivity {
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();


    }


    public void showPostComments(UserPostEntity userPost) {
        Bundle bundle=new Bundle();
        bundle.putSerializable("postDetails",userPost);
        navController.navigate(R.id.action_userDetailsFragment_to_commentDetailsFragment,bundle);

    }

    public void show(UserDetails userDetails) {

        Bundle bundle=new Bundle();
        bundle.putSerializable("userDetails",userDetails);

        navController.navigate(R.id.action_userListFragment_to_userDetailsFragment,bundle);
    }



}