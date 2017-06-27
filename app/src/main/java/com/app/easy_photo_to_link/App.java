package com.app.easy_photo_to_link;

import android.app.Application;

import com.app.easy_photo_to_link.dagger.AppComponent;
import com.app.easy_photo_to_link.dagger.AppModule;
import com.app.easy_photo_to_link.dagger.DaggerAppComponent;


public class App extends Application
{
    private static AppComponent component;
    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }
    public static AppComponent getComponent()
    {
        return component;
    }
}
