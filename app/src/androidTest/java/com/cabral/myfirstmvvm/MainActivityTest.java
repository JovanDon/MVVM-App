package com.cabral.myfirstmvvm;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.cabral.myfirstmvvm.ui.fragments.UserDetailsFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void init(){//start the Nav Host Fragment before tests begin
        activityActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
    }

    @Test
    public void useAppContext(){
        Context appContext= InstrumentationRegistry.getInstrumentation().getTargetContext();
        Log.e("Error",appContext.toString());
        assertEquals("com.cabral.myfirstmvvm",appContext.getPackageName());
    }


}