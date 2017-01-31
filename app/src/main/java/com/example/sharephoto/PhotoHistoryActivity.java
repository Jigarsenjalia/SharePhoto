package com.example.sharephoto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;

import com.example.sharephoto.dbwork.WorkDB;
import com.example.sharephoto.dummy.PhotoContent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoHistoryActivity extends AppCompatActivity implements OnListFragmentInteractionListener {


    @BindView(R.id.buttonClearAllHistory)
    AppCompatTextView buttonClearAllHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_history);
        ButterKnife.bind(this);
    }

    @Override
    public void onListFragmentInteraction(PhotoContent.PhotoItem item) {
        Log.d("Returned", item.content_link);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, item.content_link);
        startActivity(intent);
    }

//    @Override
//    public boolean onListFragmentDelete(PhotoContent.PhotoItem item) {
//        WorkDB workDB = new WorkDB(getApplicationContext());
//        boolean ans = workDB.deletePhotoHistoryItemByLink(item.content_link);
//        workDB.closeAllConnections();
//
//        return ans;
//    }

    @OnClick(R.id.buttonClearAllHistory)
    public void onClick() {
        WorkDB workDB = new WorkDB(getApplicationContext());
        workDB.deletePhotoHistory();
        workDB.closeAllConnections();
        finish();
    }
}
