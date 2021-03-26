package com.cabral.myfirstmvvm.network.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cabral.myfirstmvvm.network.db.daos.UserDao;
import com.cabral.myfirstmvvm.network.db.daos.UserPostDao;
import com.cabral.myfirstmvvm.network.db.entities.User;
import com.cabral.myfirstmvvm.network.db.entities.UserAddress;
import com.cabral.myfirstmvvm.network.db.entities.UserCompany;
import com.cabral.myfirstmvvm.network.db.entities.UserPost;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database( entities = {User.class, UserAddress.class, UserCompany.class, UserPost.class},version =1, exportSchema = false)
public abstract class RoomDb extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract UserPostDao userPostDao();

    private RoomDatabase INSTANCE;
    private final static int NumberOfThreads=4;
    public final static ExecutorService databaseWriteExcecutor= Executors.newFixedThreadPool(NumberOfThreads);

    public RoomDatabase getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (RoomDatabase.class){
            if(INSTANCE==null){
                INSTANCE= Room.databaseBuilder(context.getApplicationContext(),RoomDatabase.class,"user_profiler_db").build();
            }
            }
        }
        return INSTANCE;
    }

}
