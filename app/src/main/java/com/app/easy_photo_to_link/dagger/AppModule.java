package com.app.easy_photo_to_link.dagger;

import android.content.Context;
import android.support.annotation.NonNull;

import com.app.easy_photo_to_link.dbwork.DBWork;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Владислав on 23.06.2017.
 */
@Module
public class AppModule {
    Context mContext = null;
    public AppModule(@NonNull Context context) {
        mContext = context;
    }
    @Provides
    @Singleton
    public DBWork provideDBWork()
    {
        return new DBWork(mContext);
    }
}
