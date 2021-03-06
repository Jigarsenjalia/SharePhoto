package com.app.easy_photo_to_link.activity;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.easy_photo_to_link.App;
import com.app.easy_photo_to_link.R;
import com.app.easy_photo_to_link.db.DBWork;
import com.app.easy_photo_to_link.helper.AlbumStorageDirFactory;
import com.app.easy_photo_to_link.helper.BaseAlbumDirFactory;
import com.app.easy_photo_to_link.helper.FroyoAlbumDirFactory;
import com.app.easy_photo_to_link.helper.FileHelper;
import com.app.easy_photo_to_link.api.ImageService;
import com.app.easy_photo_to_link.api.ResponseData;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.easy_photo_to_link.api.ImageService.retrofit;

public class MainActivity extends AppCompatActivity implements Callback<ResponseData>{

    public static final int RESULT_GALLERY_PHOTO = 1;
    public static final int UPLOAD_IMAGE_NOTIFICATION_ID = 129;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @BindView(R.id.buttonStartFromGallery)
    Button buttonStart;
    @BindView(R.id.buttonHistory)
    Button buttonHistory;
    //
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    ImageService apiService =
            retrofit.create(ImageService.class);
    File requestImage = null;
    //Notification
    NotificationManager mNotificationManager;
    NotificationCompat.Builder nBuilder;

    private FirebaseAnalytics mFirebaseAnalytics;
    private FileHelper fileHelper;

    @Inject
    DBWork databaseWork;

    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Inject dependence
        App.getComponent().injectMainActivity(this);
        ButterKnife.bind(this);
        Fabric.with(this, new Crashlytics());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //
        fileHelper = new FileHelper(getApplicationContext());
        checkPermission();
        checkForHistory();
        buildNotificationForUpload();
        //
        Intent intent = getIntent();
        if (intent.getAction() != null) {
            callFromAnotherApp(intent);
        }
    }

    /*
     * Builds notification which displays process of uploading of Image
     *
     */
    private void buildNotificationForUpload() {
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nBuilder = new NotificationCompat.Builder(this);
        nBuilder.setContentTitle("Picture Uploading")
                .setContentText("Download in progress...")
                .setSmallIcon(R.drawable.ic_notify_uploaded);
    }

    /*
     * Set an indeterminate type for progress bar in notification
     *
     */
    private void showUploadingProgressNotification() {
        nBuilder.setProgress(0, 0, true);
        mNotificationManager.notify(UPLOAD_IMAGE_NOTIFICATION_ID, nBuilder.build());
    }

    /*
     * Builds notification when Photo have been uploaded
     *
     */
    private void showUploadedImageNotificationWithPendingIntent(Intent intentForPhotoShare) {
        nBuilder.setContentText("Upload complete")
                .setProgress(0, 0, false);
        nBuilder.setContentIntent(PendingIntent.getActivity(this, 0, intentForPhotoShare, 0));
        nBuilder.setAutoCancel(true);
        mNotificationManager.notify(UPLOAD_IMAGE_NOTIFICATION_ID, nBuilder.build());
    }

    private Intent createShareIntent(String imageLink) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, imageLink);
        return intent;
    }

    /*
     * Check Internet connection
     *
     * @return if mobile has connection
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /*
     * Upload image when the application have called from another Activity
     *
     */
    private void callFromAnotherApp(Intent intent) {
        if (!isOnline()) {
            Toast.makeText(this, "No internet connection...", Toast.LENGTH_LONG).show();
            finish();
        }
        String action = intent.getAction();
        String type = intent.getType();
        Uri clipDataUri = intent.getClipData().getItemAt(0).getUri();
        String typeUriScheme = clipDataUri.getScheme();


        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                if (typeUriScheme.equals("file")) {
                    requestImage = new File(clipDataUri.getPath());
                    uploadImage();
                } else if (typeUriScheme.equals("content")) {
                    requestImage = fileHelper.getFileFromUriByIS(clipDataUri);
                    uploadImage();
                }


            }
        }
    }

    private void checkForHistory() {
        if (databaseWork.isHasHistory()) {
            buttonHistory.setVisibility(View.VISIBLE);
        } else {
            buttonHistory.setVisibility(View.GONE);
        }
    }

    private void checkPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForHistory();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }

    }

    @OnClick(R.id.buttonStartFromGallery)
    public void onClick() {
        if (isOnline()) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_GALLERY_PHOTO);
        } else {
            Toast.makeText(this, "No internet connection...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            switch (requestCode) {
                case RESULT_GALLERY_PHOTO:
                    requestImage = fileHelper.getFileFromUri(data.getData());
                    uploadImage();
                    break;
            }
        }
    }
    /*
     * Upload image int hosting by Retrofit
     */
    public void uploadImage() {
        showUploadingProgressNotification();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                if (requestImage == null) {
                    requestImage = new File(mCurrentPhotoPath);
                }
                // MultipartBody.Part is used to send also the actual file name
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), requestImage);
                MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("upload", requestImage.getName(), requestFile);

                Call<ResponseData> call = apiService.uploadImage(multipartBody);
                call.enqueue(MainActivity.this);
                requestImage = null;

            }
        });
    }
    public void writePhotoToDB(String imgUrl, String thumbUrl) {
        databaseWork.writePhotoDataToDB(imgUrl, thumbUrl);
    }
    public void logPhotoUploadedEvent()
    {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Image uploaded!");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @OnClick(R.id.buttonHistory)
    public void onClickHistory() {
        Bundle bundle = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            bundle = ActivityOptions
                    .makeSceneTransitionAnimation(this)
                    .toBundle();
        }
        Intent intent = new Intent(this, PhotoHistoryActivity.class);
        startActivity(intent, bundle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
        if (response.isSuccessful()) {
            Toast.makeText(MainActivity.this, "Photo uploaded", Toast.LENGTH_SHORT).show();
            logPhotoUploadedEvent();
            //Getting data from response
            String imgUrlResult = response.body().getImageUrl().getImg_url();
            String thumbUrlResult = response.body().getImageUrl().getThumb_url();
            //
            writePhotoToDB(imgUrlResult, thumbUrlResult);
            showUploadedImageNotificationWithPendingIntent(createShareIntent(imgUrlResult));
            buttonHistory.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<ResponseData> call, Throwable t) {
        Toast.makeText(MainActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
