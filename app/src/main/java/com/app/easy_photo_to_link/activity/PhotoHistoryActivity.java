package com.app.easy_photo_to_link.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.easy_photo_to_link.R;

public class PhotoHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_history);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
