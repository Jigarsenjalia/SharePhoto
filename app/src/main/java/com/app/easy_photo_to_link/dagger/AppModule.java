package com.app.easy_photo_to_link.dagger;

import android.content.Context;
import android.support.annotation.NonNull;

import com.app.easy_photo_to_link.db.DBWork;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Class Module that contains methods(Providers) for Dependency Injections
 *
 */
@Module
public class AppModule {
    private Context mContext = null;
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
