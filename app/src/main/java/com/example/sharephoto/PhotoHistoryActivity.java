package com.example.sharephoto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.sharephoto.dummy.PhotoContent;

public class PhotoHistoryActivity extends AppCompatActivity implements OnListFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_history);
    }

    @Override
    public void onListFragmentInteraction(PhotoContent.PhotoItem item) {
        Log.d("Returned", item.content_link);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, item.content_link);
        startActivity(intent);
    }
}
