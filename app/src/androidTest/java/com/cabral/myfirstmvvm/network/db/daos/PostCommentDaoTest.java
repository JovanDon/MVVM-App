package com.cabral.myfirstmvvm.network.db.daos;

import android.database.sqlite.SQLiteConstraintException;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cabral.myfirstmvvm.network.db.RoomDb;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class PostCommentDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private RoomDb mDatabase;
    private PostCommentDao postCommentDao;

    @Before
    public void initDb() throws Exception {
        // using an in-memory database because the information stored here disappears when the process is killed
        mDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                RoomDb.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();

        postCommentDao=mDatabase.postCommentDao();
    }


    @Test
    public void cantInsertCommentWithoutAPost() throws InterruptedException {
        try {
            postCommentDao.insert(TestData.postCommentData);

            fail("SQLiteConstraintException expected");
        } catch (SQLiteConstraintException ignored) {

        }
    }

    @After
    public void closeDb() throws Exception {
        mDatabase.close();
    }


}