package com.cabral.myfirstmvvm.network.db.daos;

import android.database.sqlite.SQLiteConstraintException;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cabral.myfirstmvvm.LiveDataTestUtil;
import com.cabral.myfirstmvvm.network.db.RoomDb;
import com.cabral.myfirstmvvm.network.db.entities.User;
import com.cabral.myfirstmvvm.network.db.relations.UserDetailsRelation;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.contains;

@RunWith(AndroidJUnit4.class)
public class UserDaoTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private RoomDb mDatabase;
    private UserDao mUserDao;

    @Before
    public void initDb() throws Exception {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        mDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                RoomDb.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();
        mUserDao=mDatabase.userDao();
    }

    @Test
    public void canInsertUser()throws Exception {
        String testEmail="mutesasirajovan@gmail.com";
        TestData.userTestData.setEmail(testEmail);
        RoomDb.insertUserData(mDatabase,TestData.userTestData,TestData.addressTestData,TestData.companyTestData);

        List<UserDetailsRelation> users = LiveDataTestUtil.getValue( mUserDao.getUsers() );

        assertNotNull(users);

        assertTrue(users.size()>0);

        List<String> emailList=new ArrayList<>();
        for (UserDetailsRelation user :users) {
            emailList.add(user.userWithAddress.user.getEmail());
        }

       assertTrue(emailList.contains(testEmail));
    }

    @After
    public void closeDb() throws Exception {
        mDatabase.close();
    }

}