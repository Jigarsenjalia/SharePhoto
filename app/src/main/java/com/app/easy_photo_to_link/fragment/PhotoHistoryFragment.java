package com.app.easy_photo_to_link.fragment;

import android.content.Context;
 import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.easy_photo_to_link.App;
import com.app.easy_photo_to_link.R;
import com.app.easy_photo_to_link.adapter.MyPhotoHistoryRecyclerViewAdapter;
import com.app.easy_photo_to_link.db.DBWork;
import com.app.easy_photo_to_link.model.PhotoContent;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

import javax.inject.Inject;

/**
 * A fragment representing a list of Photos.
 * <p/>
 */
public class PhotoHistoryFragment extends Fragment implements MyPhotoHistoryRecyclerViewAdapter.OnChangeItemListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private PhotoContent photoContent;
    @Inject
    DBWork workDB;
    private Context applicationContext;
    private ActionBar actionBar;
    private TextView toolbarTitleView;

    //Menu items
    private MenuItem clearSelectedMenuItem;
    private MenuItem clearAllMenuItem;

    private MyPhotoHistoryRecyclerViewAdapter recyclerViewAdapter;

    public PhotoHistoryFragment() {
        photoContent = new PhotoContent();
    }

    @SuppressWarnings("unused")
    public static PhotoHistoryFragment newInstance(int columnCount) {
        PhotoHistoryFragment fragment = new PhotoHistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_history, menu);
        clearAllMenuItem = menu.findItem(R.id.menu_clear_all);
        clearSelectedMenuItem = menu.findItem(R.id.menu_clear_selected);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_clear_all: {
                execActionClearAllHistory();
                break;
            }
            case R.id.menu_clear_selected:
            {
                execActionClearSelectedHistory();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent().injectPhotoHistoryFragment(this);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        photoContent.setPhotoItemsFromCursor(workDB.getCursorHistory());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
            view = inflater.inflate(R.layout.fragment_photohistory_list, container, false);
        setUpRecyclerView(view);
        setUpActionBar(view);
        return view;
    }

    /*
     * Deletes Photo History which are selected
      *
     */
    public void deleteSelectedItems()
    {
        for (MyPhotoHistoryRecyclerViewAdapter.ViewHolder holder :
                recyclerViewAdapter.selectedItems) {
            recyclerViewAdapter.mValues.remove(holder.mItem);
            workDB.deletePhotoHistoryItemByLink(holder.mItem.mContentLink);
        }
        recyclerViewAdapter.notifyDataSetChanged();
        recyclerViewAdapter.selectedItems.clear();
        setDefaultActionForToolbar();
        if (recyclerViewAdapter.mValues.isEmpty())
            getActivity().onBackPressed();

    }

    /*
     * Sets up Recycler View with data
     *
     */
    private void setUpRecyclerView(View view)
    {
        View recycler = view.findViewById(R.id.list);
        // Set the adapter
        if (recycler instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) recycler;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerViewAdapter = new MyPhotoHistoryRecyclerViewAdapter(photoContent.ITEMS, applicationContext, this);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }

    /*
     * Sets up ToolBar for fragment
     */
    private void setUpActionBar(View view) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.historyToolBar);
        activity.setSupportActionBar(myToolbar);
        actionBar = activity.getSupportActionBar();
        setHasOptionsMenu(true);
        actionBar.setTitle("");
        toolbarTitleView = (TextView) myToolbar.findViewById(R.id.toolbar_title);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
            applicationContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void execActionClearAllHistory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(applicationContext);
        builder.setCancelable(true)
                .setPositiveButton(R.string.delete_all, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        workDB.deletePhotoHistory();
                        getActivity().onBackPressed();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setMessage("Do you want to delete all history?")
                .setTitle("Deleting");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    private void execActionClearSelectedHistory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(applicationContext);
        builder.setCancelable(true)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSelectedItems();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setTitle("Deleting");
        StringBuilder string = new StringBuilder("Do you want to delete " + recyclerViewAdapter.selectedItems.size() + " item");
        if (recyclerViewAdapter.selectedItems.size() != 1)
        {
            string.append("s");
        }
        string.append("?");
        builder.setMessage(string.toString());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onChangeItem(List<MyPhotoHistoryRecyclerViewAdapter.ViewHolder> viewHolder) {
        if (viewHolder.isEmpty())
        {
            setDefaultActionForToolbar();
        }
        else
        {
            toolbarTitleView.setText("Selected : " + viewHolder.size());
            clearAllMenuItem.setVisible(false);
            clearSelectedMenuItem.setVisible(true);

            View view = ((AppCompatActivity)getActivity()).findViewById(clearSelectedMenuItem.getItemId());
            YoYo.with(Techniques.FlipInX).duration(850).playOn(view);
            YoYo.with(Techniques.FlipInX).duration(850).playOn(toolbarTitleView);
        }
    }
    public void setDefaultActionForToolbar()
    {
        toolbarTitleView.setText(getString(R.string.history));
        clearAllMenuItem.setVisible(true);
        clearSelectedMenuItem.setVisible(false);
        View view = ((AppCompatActivity)getActivity()).findViewById(clearAllMenuItem.getItemId());
        YoYo.with(Techniques.FlipInX).duration(850).playOn(view);
        YoYo.with(Techniques.FlipInX).duration(850).playOn(toolbarTitleView);
    }


}
