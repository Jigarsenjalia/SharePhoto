package com.vladik_bakalo.sharephoto;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vladik_bakalo.sharephoto.dbwork.DBWork;
import com.vladik_bakalo.sharephoto.directorywork.AlbumStorageDirFactory;
import com.vladik_bakalo.sharephoto.directorywork.BaseAlbumDirFactory;
import com.vladik_bakalo.sharephoto.directorywork.FroyoAlbumDirFactory;
import com.vladik_bakalo.sharephoto.restwork.ImageService;
import com.vladik_bakalo.sharephoto.restwork.ResponseData;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vladik_bakalo.sharephoto.restwork.ImageService.retrofit;

public class MainActivity extends AppCompatActivity {

    public static final int RESULT_GALERY_PHOTO = 1;
    //public static final int RESULT_TEKEN_PHOTO = 2;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @BindView(R.id.buttonStartFromGallery)
    Button buttonStart;
    @BindView(R.id.buttonHistory)
    Button buttonHistory;
    //
    //
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    public Bitmap mySelectedPhoto;
    ImageService apiService =
            retrofit.create(ImageService.class);
    File requestImage = null;
    private String mCurrentPhotoPath;
    ProgressDialog progress;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // Get intent, action and MIME type
        checkPermission();
        checkForHistory();
        //
        progress = new ProgressDialog(this);
        progress.setMessage("Uploading photo... ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        //
        Intent intent = getIntent();
        if (intent.getAction() != null) {
            callFromAnotherApp(intent);
        }
        //


    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    private void callFromAnotherApp(Intent intent) {
        if (!isOnline())
        {
            Toast.makeText(this, "No internet connection...", Toast.LENGTH_LONG).show();
            finish();
        }
        String action = intent.getAction();
        String type = intent.getType();
        Uri clipDataUri = intent.getClipData().getItemAt(0).getUri();
        String typeUriScheme = clipDataUri.getScheme();


        if (Intent.ACTION_SEND.equals(action) && type != null) {
            String str =clipDataUri.getPath();
            if (type.startsWith("image/")) {
                if(typeUriScheme.equals("file"))
                {
                    requestImage = new File(clipDataUri.getPath());
                    uploadImage();
                }
                else if(typeUriScheme.equals("content")){
                    requestImage = getFileFromUriByIS(clipDataUri);
                    uploadImage();
                }


            }
        }
    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
    private void checkForHistory()
    {
        DBWork workDB = new DBWork(getApplicationContext());
        if (workDB.isHasHistory())
        {
            buttonHistory.setVisibility(View.VISIBLE);
        }
        else
        {
            buttonHistory.setVisibility(View.GONE);
        }
        workDB.closeAllConnections();
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

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    @OnClick(R.id.buttonStartFromGallery)
    public void onClick() {
        if (isOnline()) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_GALERY_PHOTO);
        }
        else
        {
            Toast.makeText(this, "No internet connection...", Toast.LENGTH_LONG).show();
        }
    }

//    public void onClickCapture() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File f = null;
//
//        try {
//            f = setUpPhotoFile();
//            mCurrentPhotoPath = f.getAbsolutePath();
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//        } catch (IOException e) {
//            e.printStackTrace();
//            f = null;
//            mCurrentPhotoPath = null;
//        }
//        startActivityForResult(intent, RESULT_TEKEN_PHOTO);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            switch (requestCode) {
                case RESULT_GALERY_PHOTO:
                    requestImage = getFileFromUri(data.getData());
                    uploadImage();
                    break;
//                case RESULT_TEKEN_PHOTO:
//                    extras = data.getExtras();
//                    mySelectedPhoto = (Bitmap) extras.get("data");
//                    uploadImage();
//                    break;
            }
        }
    }
    public File getFileFromUriByIS(Uri photoPath)
    {
        InputStream inputStream;
        File finishFile = null;
        Bitmap bit;
        try {
            inputStream = getContentResolver().openInputStream(photoPath);
            bit = BitmapFactory.decodeStream(inputStream);
            finishFile = persistImage(bit, "tratata");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return finishFile;
    }
    private  File persistImage(Bitmap bitmap, String name) {
        File filesDir = getApplicationContext().getFilesDir();
        File imageFile = new File(filesDir, name + ".png");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }
    public File getFileFromUri(Uri photoPath) {
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(photoPath, filePath, null, null, null);
        cursor.moveToFirst();
        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
        cursor.close();
        return new File(imagePath);
    }
//    public File getFileFromBitmap(Context context, Bitmap bitmap) {
//        File filesDir = context.getFilesDir();
//        File f = new File(filesDir, "MyPhoto.jpg");
//        try {
//            f.createNewFile();
//            //Convert bitmap to byte array
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
//            byte[] bitmapdata = bos.toByteArray();
//
//            //write the bytes in file
//            FileOutputStream fos = new FileOutputStream(f);
//            fos.write(bitmapdata);
//            fos.flush();
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return f;
//    }

    public void uploadImage() {
        progress.show();
        //progressBar.setVisibility(ProgressBar.VISIBLE);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                //MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");
                //MediaType MEDIA_TYPE_PNG = MediaType.parse("multipart/form-data");
                if (requestImage == null) {
                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    //requestImage = getFileFromBitmap(getApplicationContext(), mySelectedPhoto);
                    requestImage = new File(mCurrentPhotoPath);
                }
                // MultipartBody.Part is used to send also the actual file name
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), requestImage);

                MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("upload", requestImage.getName(), requestFile);


                Call<ResponseData> call = apiService.uploadImage(multipartBody);
                call.enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        //progressBar.setVisibility(ProgressBar.GONE);
                        progress.dismiss();
                        if (response.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Photo uploaded", Toast.LENGTH_SHORT).show();
                            String imgUrlResult = response.body().getImageUrl().getImg_url();
                            String thumbUrlResult = response.body().getImageUrl().getThumb_url();
                            writePhotoToDB(imgUrlResult, thumbUrlResult);
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_TEXT, imgUrlResult);
                            startActivity(intent);
                            buttonHistory.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                requestImage = null;

            }
        });
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    private String getAlbumName() {
        return getString(R.string.album_name);
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
    public void writePhotoToDB(String imgUrl, String thumbUrl)
    {
        DBWork workDB = new DBWork(getApplicationContext());
        workDB.writePhotoDataToDB(imgUrl, thumbUrl);
        workDB.closeAllConnections();
    }
    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //AppIndex.AppIndexApi.end(client, getIndexApiAction());
        //client.disconnect();
    }

    @OnClick(R.id.buttonHistory)
    public void onClickHistory() {
        Intent intent = new Intent(this, PhotoHistoryActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
