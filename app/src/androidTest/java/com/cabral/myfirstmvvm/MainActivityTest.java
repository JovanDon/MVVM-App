package com.cabral.myfirstmvvm;

import junit.framework.TestCase;

import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;

public class MainActivityTest extends TestCase {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void disableRecyclerViewAnimations() {
        // Disable RecyclerView animations
        EspressoTestUtil.disableAnimations(mActivityRule);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
    }

    public void testAttachBaseContext() {
    }
}