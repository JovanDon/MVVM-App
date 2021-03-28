/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cabral.myfirstmvvm;

import android.app.Application;
import com.cabral.myfirstmvvm.network.UsersDataRepository;
import com.cabral.myfirstmvvm.network.db.RoomDb;


public class BasicApp extends Application {

    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();
    }

    public RoomDb getDatabase() {
        return RoomDb.getDatabase(this);
    }

    public UsersDataRepository getRepository() {
        return UsersDataRepository.getInstance(this);
    }
}
