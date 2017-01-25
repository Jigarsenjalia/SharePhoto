package com.example.sharephoto;

import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Владислав on 25.01.2017.
 */

public class MyCallBack implements Callback<ResponseData> {
    @Override
    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

//        if (response.isSuccessful()) {
//            Toast.makeText(MainActivity.this, "Photo uploaded", Toast.LENGTH_SHORT).show();
//            String imgUrlResult = response.body().getImageUrl().getImg_url();
//            writePhotoToDB(imgUrlResult);
//            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.setType("text/plain");
//            intent.putExtra(Intent.EXTRA_TEXT, imgUrlResult);
//            startActivity(intent);
//        } else {
//            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onFailure(Call<ResponseData> call, Throwable t) {

    }
}
