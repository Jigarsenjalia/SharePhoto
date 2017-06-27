package com.app.easy_photo_to_link.dagger;

import android.content.Context;
import android.support.annotation.NonNull;

import com.app.easy_photo_to_link.MainActivity;
import com.app.easy_photo_to_link.PhotoHistoryActivity;
import com.app.easy_photo_to_link.PhotoHistoryFragment;
import com.app.easy_photo_to_link.dbwork.DBWork;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Владислав on 23.06.2017.
 */

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
    void injectMainActivity(MainActivity activity);
    void injectPhotoHistoryFragment(PhotoHistoryFragment fragment);

}
