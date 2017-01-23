package com.example.sharephoto;

/**
 * Created by Владислав on 28.12.2016.
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

    @Override
    public String toString() {
        return "ImageData{" +
                "img_url='" + img_url + '\'' +
                '}';
    }
}
