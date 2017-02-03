package com.vladik_bakalo.sharephoto.restwork;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Владислав on 28.12.2016.
 */

public class ResponseData {
    @SerializedName("data")
    @Expose
    private ImageData imageData;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("status_txt")
    @Expose
    private String statusTxt;
    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ImageData getImageUrl() {
        return imageData;
    }

    public void setImageUrl(ImageData imageUrl) {
        this.imageData = imageUrl;
    }

    public String getStatusTxt() {
        return statusTxt;
    }

    public void setStatusTxt(String statusTxt) {
        this.statusTxt = statusTxt;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "imageUrl='" + imageData + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", statusTxt='" + statusTxt + '\'' +
                '}';
    }
}
