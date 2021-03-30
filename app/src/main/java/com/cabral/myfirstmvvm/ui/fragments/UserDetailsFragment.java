package com.cabral.myfirstmvvm.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cabral.myfirstmvvm.MainActivity;
import com.cabral.myfirstmvvm.R;
import com.cabral.myfirstmvvm.databinding.FragmentUserDetailsBinding;
import com.cabral.myfirstmvvm.network.db.entities.UserPostEntity;
import com.cabral.myfirstmvvm.responses.UserDetails;
import com.cabral.myfirstmvvm.ui.adapters.UserPostAdapter;
import com.cabral.myfirstmvvm.ui.callbacks.PostClickCallback;
import com.cabral.myfirstmvvm.util.Resource;
import com.cabral.myfirstmvvm.viewmodels.UserViewModel;

import java.util.List;

public class UserDetailsFragment extends Fragment {

    private FragmentUserDetailsBinding mBinding;
    private UserPostAdapter mUserPostAdapter;
    private static final String KEY_USER = "userDetails";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_user_details, container, false);

        mUserPostAdapter = new UserPostAdapter(mUserClickCallback);
        mBinding.postList.setAdapter(mUserPostAdapter);

        mBinding.toolbar.setTitle(getString(R.string.user_details));
        //mBinding.toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        mBinding.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow));


        ((AppCompatActivity)getActivity()).setSupportActionBar(mBinding.toolbar);


        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment navHostFragment =
                        (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

                NavController navController = navHostFragment.getNavController();

                navController.navigate(R.id.action_userDetailsFragment_to_userListFragment);
            }
        });


        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserDetails userDetails=(UserDetails) requireArguments().getSerializable(KEY_USER);

        UserViewModel.Factory factory=new UserViewModel.Factory(
                requireActivity().getApplication(),
                userDetails
        );

        UserViewModel userViewModel= new ViewModelProvider(this,factory).get(UserViewModel.class);

        mBinding.setIsLoading(true);
        subscribeUi(userViewModel.getmUserPost());
        mBinding.setUserDetals(userDetails);
    }

    private void subscribeUi(LiveData<Resource<List<UserPostEntity>>> liveData) {
        // Update the list when the data changes
        liveData.observe(getViewLifecycleOwner(), myPosts -> {
            if (myPosts.data != null) {
                mBinding.setIsLoading(false);
                mUserPostAdapter.setPostList(myPosts.data);

            } else {
                mBinding.setIsLoading(true);
            }
            // espresso does not know how to wait for data binding's loop so we execute changes
            mBinding.executePendingBindings();
        });
    }

    private final PostClickCallback mUserClickCallback = userPosts -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
             ((MainActivity) requireActivity()).showPostComments(userPosts);
        }
    };




}