package com.cabral.myfirstmvvm.ui.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cabral.myfirstmvvm.MainActivity;
import com.cabral.myfirstmvvm.R;
import com.cabral.myfirstmvvm.databinding.UserListFragmentBinding;
import com.cabral.myfirstmvvm.models.UserDetails;
import com.cabral.myfirstmvvm.ui.adapters.UserAdapter;
import com.cabral.myfirstmvvm.ui.callbacks.UserClickCallback;
import com.cabral.myfirstmvvm.viewmodels.UserListViewModel;

import java.util.List;

public class UserListFragment extends Fragment {

    public static final String TAG = "UserListFragment";

    private UserAdapter mUserAdapter;

    private UserListFragmentBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding=DataBindingUtil.inflate(inflater,R.layout.user_list_fragment, container, false);

        mUserAdapter = new UserAdapter(mUserClickCallback);
        mBinding.usersList.setAdapter(mUserAdapter);
        mBinding.toolbar.setTitle(getString(R.string.user_list_title));

        ((AppCompatActivity)getActivity()).setSupportActionBar(mBinding.toolbar);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserListViewModel mViewModel = new ViewModelProvider(this).get(UserListViewModel.class);
        subscribeUi(mViewModel.getUsers());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }
    @Override
    public void onDestroyView() {
        mBinding = null;
        mUserAdapter = null;
        super.onDestroyView();
    }

    private void subscribeUi(LiveData<List<UserDetails>> liveData){
        liveData.observe(getViewLifecycleOwner(),myUsers->{
            if (myUsers != null) {
                mBinding.setIsLoading(false);
                mUserAdapter.setUserList(myUsers);
            } else {
                mBinding.setIsLoading(true);
            }
            // espresso does not know how to wait for data binding's loop so we execute changes
            // sync.
            mBinding.executePendingBindings();
        });
    }

    private final UserClickCallback mUserClickCallback = userDetails -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            ((MainActivity) requireActivity()).show(userDetails);
        }
    };

}