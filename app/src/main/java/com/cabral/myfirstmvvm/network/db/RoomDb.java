package com.cabral.myfirstmvvm.network.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cabral.myfirstmvvm.network.db.daos.UserAddressDao;
import com.cabral.myfirstmvvm.network.db.daos.UserCompanyDao;
import com.cabral.myfirstmvvm.network.db.daos.UserDao;
import com.cabral.myfirstmvvm.network.db.daos.UserPostDao;
import com.cabral.myfirstmvvm.network.db.entities.Address;
import com.cabral.myfirstmvvm.network.db.entities.User;
import com.cabral.myfirstmvvm.network.db.entities.UserCompany;
import com.cabral.myfirstmvvm.network.db.entities.UserPost;

@Database( entities = {User.class, Address.class, UserCompany.class, UserPost.class},version =2, exportSchema = false)
public abstract class RoomDb extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract UserAddressDao userAddressDao();
    public abstract UserCompanyDao userCompanyDao();
    public abstract UserPostDao userPostDao();

    private static RoomDb INSTANCE;

    public static RoomDb getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (RoomDatabase.class){
            if(INSTANCE==null){
                INSTANCE= Room.databaseBuilder(context.getApplicationContext(),RoomDb.class,"user_profiler_db").build();
            }
            }
        }
        return INSTANCE;
    }

}
