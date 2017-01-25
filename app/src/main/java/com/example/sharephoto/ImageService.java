package com.example.sharephoto;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


/**
 * Created by Владислав on 28.12.2016.
 */

public interface ImageService {
    public static final String BASE_URL = "http://uploads.im/";
    @Multipart
    @POST("api")
    Call<ResponseData> uploadImage( @Part MultipartBody.Part image);


    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
