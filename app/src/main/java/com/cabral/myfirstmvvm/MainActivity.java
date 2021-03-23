package com.cabral.myfirstmvvm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.cabral.myfirstmvvm.models.UserDetails;
import com.cabral.myfirstmvvm.models.UserPost;
import com.cabral.myfirstmvvm.ui.fragments.UserDetailsFragment;
import com.cabral.myfirstmvvm.ui.fragments.UserListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, UserListFragment.newInstance())
                    .commitNow();
        }


    }

    public void showPostComments(UserPost userPosts) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, UserListFragment.newInstance())
                .commitNow();

    }

    public void show(UserDetails userDetails) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, UserDetailsFragment.newInstance(userDetails.getUser_id()))
                .commitNow();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // Pop previous Fragment
            getSupportFragmentManager().popBackStack();

        }else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, UserListFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}