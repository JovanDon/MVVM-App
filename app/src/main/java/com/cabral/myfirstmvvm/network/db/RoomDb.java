package com.cabral.myfirstmvvm.network.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cabral.myfirstmvvm.network.db.daos.PostCommentDao;
import com.cabral.myfirstmvvm.network.db.daos.UserAddressDao;
import com.cabral.myfirstmvvm.network.db.daos.UserCompanyDao;
import com.cabral.myfirstmvvm.network.db.daos.UserDao;
import com.cabral.myfirstmvvm.network.db.daos.UserPostDao;
import com.cabral.myfirstmvvm.network.db.entities.Address;
import com.cabral.myfirstmvvm.network.db.entities.PostComment;
import com.cabral.myfirstmvvm.network.db.entities.User;
import com.cabral.myfirstmvvm.network.db.entities.UserCompany;
import com.cabral.myfirstmvvm.network.db.entities.UserPostEntity;

@Database( entities = {User.class, Address.class, UserCompany.class, UserPostEntity.class, PostComment.class},version =1, exportSchema = false)
public abstract class RoomDb extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract UserAddressDao userAddressDao();
    public abstract UserCompanyDao userCompanyDao();
    public abstract UserPostDao userPostDao();
    public abstract PostCommentDao postCommentDao();

    private static RoomDb INSTANCE;

    public static RoomDb getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (RoomDatabase.class){
            if(INSTANCE==null){
                INSTANCE= Room.databaseBuilder(context.getApplicationContext(),RoomDb.class,"user_profilerDB")
                        //.addMigrations(MIGRATION_3_4)
                        .build();
            }
            }
        }
        return INSTANCE;
    }

    public static void insertUserData(final RoomDb database,
                                       final User user,
                                       final Address address,
                                       final UserCompany company) {
        database.runInTransaction(() -> {
            database.userDao().insertUser(user);
            database.userAddressDao().insertUserAddress(address);
            database.userCompanyDao().insertUserCompany(company);
        });

    }
//
//    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("CREATE TABLE IF NOT EXISTS `post_comment` (`id` INTEGER NOT NULL, `postId` INTEGER, " +
//                    "`name` TEXT, `email` TEXT, `body` TEXT, PRIMARY KEY(`id`))");
//        }
//    };



}
