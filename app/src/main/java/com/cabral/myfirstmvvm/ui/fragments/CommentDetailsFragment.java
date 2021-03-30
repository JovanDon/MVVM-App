package com.cabral.myfirstmvvm.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cabral.myfirstmvvm.R;
import com.cabral.myfirstmvvm.databinding.CommentListLayoutBinding;
import com.cabral.myfirstmvvm.network.db.entities.PostComment;
import com.cabral.myfirstmvvm.network.db.entities.UserPostEntity;
import com.cabral.myfirstmvvm.ui.adapters.PostCommentsAdapter;
import com.cabral.myfirstmvvm.util.Resource;
import com.cabral.myfirstmvvm.viewmodels.PostCommentViewModel;

import java.util.List;

public class CommentDetailsFragment extends Fragment {

    private PostCommentsAdapter postCommentsAdapter;
    private CommentListLayoutBinding mBinding;
    private static final String KEY_DETAILS = "postDetails";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding= DataBindingUtil.inflate(inflater, R.layout.comment_list_layout, container, false);

        postCommentsAdapter = new PostCommentsAdapter();

        mBinding.commentList.setAdapter(postCommentsAdapter);
        mBinding.toolbar.setTitle(getString(R.string.post_deatils));

        mBinding.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow));


        ((AppCompatActivity)getActivity()).setSupportActionBar(mBinding.toolbar);


        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment navHostFragment =
                        (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

                NavController navController = navHostFragment.getNavController();

                navController.popBackStack();
            }
        });

        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserPostEntity userPostDetails=(UserPostEntity) requireArguments().getSerializable(KEY_DETAILS);
        PostCommentViewModel.Factory factory= new PostCommentViewModel.Factory(
                requireActivity().getApplication(),
                userPostDetails.getId()
        );

        PostCommentViewModel viewModel= new ViewModelProvider(this,factory).get(PostCommentViewModel.class);


        mBinding.commentList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(mBinding.commentList.canScrollVertically(1)){
                    viewModel.getUsersNextPage();

                }
            }
        });

        mBinding.setIsLoading(true);
        subscribeUi(viewModel.getComments());
        mBinding.setUserPost(userPostDetails);
    }

    private void subscribeUi(LiveData<Resource<List<PostComment>>> liveData) {
        // Update the list when the data changes
        liveData.observe(getViewLifecycleOwner(), myComments -> {
            if (myComments.data != null) {
                mBinding.setIsLoading(false);
                postCommentsAdapter.setComments(myComments.data);

            } else {
                mBinding.setIsLoading(true);
            }
            // espresso does not know how to wait for data binding's loop so we execute changes
            mBinding.executePendingBindings();
        });
    }


}
