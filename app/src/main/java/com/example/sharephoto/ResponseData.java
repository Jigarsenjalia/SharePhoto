package com.example.sharephoto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Владислав on 28.12.2016.
 */

public class ResponseData {
    @SerializedName("data")
    @Expose
    private ImageData imageUrl;
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
        return imageUrl;
    }

    public void setImageUrl(ImageData imageUrl) {
        this.imageUrl = imageUrl;
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
                "imageUrl='" + imageUrl + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", statusTxt='" + statusTxt + '\'' +
                '}';
    }
}
