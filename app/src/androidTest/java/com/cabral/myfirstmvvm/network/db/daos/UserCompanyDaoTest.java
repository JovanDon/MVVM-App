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
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class UserCompanyDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private RoomDb mDatabase;
    private UserCompanyDao mUserCompanyDao;

    @Before
    public void initDb() throws Exception {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        mDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                RoomDb.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();
        mUserCompanyDao=mDatabase.userCompanyDao();
    }

    @Test
    public void cantInsertCompanyWithoutUser() throws InterruptedException {
        try {
            mUserCompanyDao.insertUserCompany(TestData.companyTestData);

            fail("SQLiteConstraintException expected");
        } catch (SQLiteConstraintException ignored) {

        }
    }

    @After
    public void closeDb() throws Exception {
        mDatabase.close();
    }

}