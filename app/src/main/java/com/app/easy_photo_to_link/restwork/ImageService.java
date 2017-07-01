package com.app.easy_photo_to_link.restwork;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


/**
 * Java interface for Image Hosting API
 */

public interface ImageService {
    String BASE_URL = "http://uploads.im/";
    @Multipart
    @POST("api")
    Call<ResponseData> uploadImage(@Part MultipartBody.Part image);


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
