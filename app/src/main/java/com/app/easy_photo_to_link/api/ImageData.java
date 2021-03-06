package com.app.easy_photo_to_link.api;

/**
 * This class is part of responding data from request
 */

public class ImageData
{
    private String img_url;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    private String thumb_url;

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    @Override
    public String toString() {
        return "ImageData{" +
                "img_url='" + img_url + '\'' +
                '}';
    }
}
