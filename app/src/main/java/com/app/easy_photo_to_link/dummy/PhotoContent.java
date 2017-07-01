package com.app.easy_photo_to_link.dummy;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces.
 * <p>
 */
public class PhotoContent {


    public final List<PhotoItem> ITEMS = new ArrayList<PhotoItem>();
    private void addItem(PhotoItem item) {
        ITEMS.add(item);
    }
    /*
     * Fill list by cursor
     *
     * @param cursorOfPhotoItems The cursor of Photo History Items from Data Base
     */
    public void setPhotoItemsFromCursor(Cursor cursorOfPhotoItems)
    {
        Cursor cursorData = cursorOfPhotoItems;
        if (cursorData.moveToFirst()) {
            PhotoItem photoItem;
            do{
                photoItem = new PhotoItem(cursorData.getString(0), cursorData.getString(1), cursorData.getString(2));
                addItem(photoItem);
            } while (cursorData.moveToNext());
        }
    }

    /*
     * Inner class that describe Photo History Objects for Recycler View
     *
     */
    public static class PhotoItem {
        public final String mDateTime;
        public final String mContentLink;
        public final String mThumbLink;

        /*
         * @param dateTime The date of uploading Image on  server
         * @param contentLink The link of Image in the server
         */
        public PhotoItem(String dateTime, String contentLink, String thumbLink) {
            this.mDateTime = dateTime;
            this.mContentLink = contentLink;
            this.mThumbLink = thumbLink;
        }

        @Override
        public String toString() {
            return mContentLink;
        }
    }
}
