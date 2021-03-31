package com.cabral.myfirstmvvm.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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
import com.cabral.myfirstmvvm.util.DialogLoader;
import com.cabral.myfirstmvvm.util.Resource;
import com.cabral.myfirstmvvm.viewmodels.PostCommentViewModel;

import java.util.List;

public class CommentDetailsFragment extends Fragment {



    private PostCommentsAdapter postCommentsAdapter;
    private CommentListLayoutBinding mBinding;
    private static final String KEY_DETAILS = "postDetails";
    public static  PostCommentViewModel viewModel;
    public static boolean updateMode=false;
    public static EditText newCommentName;
    public static EditText newComment;
    public static PostComment commentToUpdate;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding= DataBindingUtil.inflate(inflater, R.layout.comment_list_layout, container, false);

        postCommentsAdapter = new PostCommentsAdapter(getContext(),getViewLifecycleOwner());

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
        newCommentName=mBinding.commentName;
        newComment=mBinding.newComment;
        UserPostEntity userPostDetails=(UserPostEntity) requireArguments().getSerializable(KEY_DETAILS);
        PostCommentViewModel.Factory factory= new PostCommentViewModel.Factory(
                requireActivity().getApplication(),
                userPostDetails
        );

        viewModel= new ViewModelProvider(this,factory).get(PostCommentViewModel.class);


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


        mBinding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( !mBinding.newComment.getText().toString().isEmpty() && !mBinding.commentName.getText().toString().isEmpty() ){
                    if(updateMode){
                        updateComment(
                                new PostComment(
                                        commentToUpdate.getId(),
                                        commentToUpdate.getPostId(),
                                        mBinding.commentName.getText().toString(),
                                        commentToUpdate.getEmail(),
                                        mBinding.newComment.getText().toString()
                                )
                        );
                    }else {
                        submitNewComment(userPostDetails,viewModel);
                    }



                }

            }
        });

    }

    private void submitNewComment(UserPostEntity userPostDetails, PostCommentViewModel viewModel) {
        DialogLoader dialogLoader=new DialogLoader(getContext());
        dialogLoader.showProgressDialog();
        String sendingEmail="mutesasirajovan@gmail.com";

        PostComment posComment=new PostComment(
                0,
                userPostDetails.getId(),
                mBinding.commentName.getText().toString(),
                sendingEmail,
                mBinding.newComment.getText().toString()

        );
        LiveData<PostComment> liveData=viewModel.submitComment(posComment);

        liveData.observe(getViewLifecycleOwner(), myComment -> {
            dialogLoader.hideProgressDialog();
            if (myComment != null && myComment.getEmail().equalsIgnoreCase(sendingEmail) ) {
                Toast.makeText(getContext(), "Posted successfully", Toast.LENGTH_SHORT).show();
                mBinding.commentName.setText("");//reset Form
                mBinding.newComment.setText("");
            } else {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
            // espresso does not know how to wait for data binding's loop so we execute changes
            mBinding.executePendingBindings();
        });
    }

    private void updateComment(PostComment postComment) {
        DialogLoader dialogLoader=new DialogLoader(getContext());
        dialogLoader.showProgressDialog();

        LiveData<PostComment> liveData=CommentDetailsFragment.viewModel.updatePostComment(
                postComment
        );

        liveData.observe(getViewLifecycleOwner(), myComment -> {
            dialogLoader.hideProgressDialog();
            if (myComment != null && myComment.getEmail().equalsIgnoreCase(postComment.getEmail()) ) {
                Toast.makeText(getContext(), "Comment updated successfully", Toast.LENGTH_SHORT).show();
                mBinding.commentName.setText("");//reset Form
                mBinding.newComment.setText("");
                updateMode=false;
            } else {
                updateMode=true;
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }


        });
    }


    private void subscribeUi(LiveData<Resource<List<PostComment>>> liveData) {
        // Update the list when the data changes
        liveData.observe(getViewLifecycleOwner(), myComments -> {
            if (myComments.data != null && myComments.data.size()!=0) {
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
