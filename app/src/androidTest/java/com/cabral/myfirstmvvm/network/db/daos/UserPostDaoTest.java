package com.cabral.myfirstmvvm.network.db.daos;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cabral.myfirstmvvm.LiveDataTestUtil;
import com.cabral.myfirstmvvm.network.db.RoomDb;
import com.cabral.myfirstmvvm.network.db.entities.UserPostEntity;
import com.cabral.myfirstmvvm.network.db.relations.UserDetailsRelation;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class UserPostDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private RoomDb mDatabase;
    private UserPostDao mUserPostDao;

    @Before
    public void initDb() throws Exception {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        mDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                RoomDb.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();
        mUserPostDao=mDatabase.userPostDao();
    }

    @Test
    public void canInsertUser()throws Exception {
        String testTile="He's a bad Coder!";
        TestData.userPostData.setTitle(testTile);

        mUserPostDao.insert(TestData.userPostData);

        List<UserPostEntity> userPosts = LiveDataTestUtil.getValue( mUserPostDao.getPosts(TestData.userPostData.getUserId()) );

        assertNotNull(userPosts);

        assertTrue(userPosts.size()>0);

        List<String> postTitleList=new ArrayList<>();
        for (UserPostEntity userpost :userPosts) {
            postTitleList.add(userpost.getTitle());
        }

        assertTrue(postTitleList.contains(testTile));
    }

    @After
    public void closeDb() throws Exception {
        mDatabase.close();
    }
}