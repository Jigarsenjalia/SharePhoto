package com.app.easy_photo_to_link.dagger;

import com.app.easy_photo_to_link.activity.MainActivity;
import com.app.easy_photo_to_link.fragment.PhotoHistoryFragment;
import javax.inject.Singleton;
import dagger.Component;

/**
 * Class Component that build tree for Dependency Injections
 * Connects all Modules that describe in @Component annotation
 *
 */

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
    void injectMainActivity(MainActivity activity);
    void injectPhotoHistoryFragment(PhotoHistoryFragment fragment);

}
