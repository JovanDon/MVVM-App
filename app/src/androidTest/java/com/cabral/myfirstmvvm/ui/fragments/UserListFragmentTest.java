package com.cabral.myfirstmvvm.ui.fragments;

import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.annotation.UiThreadTest;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.cabral.myfirstmvvm.MainActivity;
import com.cabral.myfirstmvvm.R;
import com.cabral.myfirstmvvm.ui.adapters.UserAdapter;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

public class UserListFragmentTest {
    TestNavHostController navHostController;
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp() throws Exception {
        ActivityScenario.launch(MainActivity.class).onActivity(activity ->{ startUserListFragment();});
    }

    private void startUserListFragment() {
        navHostController= new TestNavHostController(ApplicationProvider.getApplicationContext());
        navHostController.setGraph(R.navigation.main_nav_graph);

    }

    @Test
    public void ensureListViewIsPresent() throws Exception {

        View viewById = activityTestRule.getActivity().findViewById(R.id.users_list);// Espresso.onView(R.id.users_list,ViewMatchers.assertThat());
        assertThat(viewById).isNotNull();
        assertThat(viewById).isInstanceOf(RecyclerView.class);
        RecyclerView recyclerView = (RecyclerView) viewById;
        UserAdapter adapter = (UserAdapter) recyclerView.getAdapter();
        assertThat(adapter).isInstanceOf(UserAdapter.class);
        assertThat(adapter.getItemCount()).isGreaterThan(0);

    }

    @Test
    public  void  navigateToUserDetails(){

        //Create Graphical FragmentScenario for UserDetails Fragment
        FragmentScenario userDetailsScenario= FragmentScenario.launchInContainer(UserDetailsFragment.class);

        //set NavigationController Property
        userDetailsScenario.onFragment(fragment-> Navigation.setViewNavController(fragment.requireView(), navHostController));

        //perform click for navigation
        Espresso.onView(ViewMatchers.withId(R.id.UserCard_item)).perform(ViewActions.click());
        assertThat(navHostController.getCurrentDestination().getId()).isEqualTo(R.id.userDetailsFragment);
    }
}