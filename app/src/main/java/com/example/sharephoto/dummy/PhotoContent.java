package com.example.sharephoto.dummy;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PhotoContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<PhotoItem> ITEMS = new ArrayList<PhotoItem>();

//    private static final int COUNT = 25;
//
//    static {
//        // Add some sample items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createDummyItem(i));
//        }
//    }

    private static void addItem(PhotoItem item) {
        ITEMS.add(item);
    }
    public void setPhotoItemsFromCursor(Cursor cursorOfPhotoItems)
    {
        Cursor cursorData = cursorOfPhotoItems;
        if (cursorData.moveToFirst()) {
            PhotoItem photoItem;
            for (int i = 0; i < cursorData.getCount(); i++) {
                photoItem = new PhotoItem(cursorData.getString(0), cursorData.getString(1));
            }
        }
    }
    /**
     * A dummy item representing a piece of content.
     */
    public static class PhotoItem {
        public final String date_time;
        public final String content_link;

        public PhotoItem(String date_time, String content_link) {
            this.date_time = date_time;
            this.content_link = content_link;
        }

        @Override
        public String toString() {
            return content_link;
        }
    }
}