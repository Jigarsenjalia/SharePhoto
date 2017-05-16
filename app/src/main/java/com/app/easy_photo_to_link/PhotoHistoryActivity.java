package com.app.easy_photo_to_link;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.app.easy_photo_to_link.dbwork.DBWork;

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
        return true;
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
