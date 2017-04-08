package com.vladik_bakalo.sharephoto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.vladik_bakalo.sharephoto.dbwork.DBWork;
import com.vladik_bakalo.sharephoto.dummy.PhotoContent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_history);
        setUpActionBar();
    }

    private void setUpActionBar()
    {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.historyToolBar);
        myToolbar.setTitle("History");
        setSupportActionBar(myToolbar);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.menu_clear:
            {
                execActionClearAllHistory();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void execActionClearAllHistory() {
        DBWork workDB = new DBWork(getApplicationContext());
        workDB.deletePhotoHistory();
        workDB.closeAllConnections();
        onBackPressed();
    }

}
