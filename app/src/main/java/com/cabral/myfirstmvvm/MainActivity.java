package com.cabral.myfirstmvvm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cabral.myfirstmvvm.models.UserDetails;
import com.cabral.myfirstmvvm.models.UserPost;

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

    public void showPostComments(UserPost userPosts) {

        navController.navigate(R.id.action_userDetailsFragment_to_userListFragment);

    }

    public void show(UserDetails userDetails) {
        Bundle bundle=new Bundle();
        bundle.putInt("user_id",userDetails.getUser_id());
        navController.navigate(R.id.action_userListFragment_to_userDetailsFragment,bundle);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // Pop previous Fragment
            getSupportFragmentManager().popBackStack();

        }else{
            navController.navigate(R.id.action_userDetailsFragment_to_userListFragment);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}