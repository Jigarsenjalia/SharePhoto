package com.example.sharephoto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sharephoto.dbwork.WorkDB;
import com.example.sharephoto.dummy.PhotoContent.PhotoItem;
import com.example.sharephoto.restwork.DownloadImageTask;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PhotoItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */

public class MyPhotoHistoryRecyclerViewAdapter extends RecyclerView.Adapter<MyPhotoHistoryRecyclerViewAdapter.ViewHolder> {

    private final List<PhotoItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyPhotoHistoryRecyclerViewAdapter(List<PhotoItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_photohistory, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mLinkView.setText(mValues.get(position).content_link);
        holder.mDateView.setText(mValues.get(position).date_time);
        new DownloadImageTask(holder.mThumbView).execute(mValues.get(position).thumb_link);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
//        holder.mView.setOnLongClickListener(new View.OnLongClickListener(){
//            @Override
//            public boolean onLongClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    boolean ans = mListener.onListFragmentDelete(holder.mItem);
//                    if(ans) {
//                        mValues.remove(holder.mItem);
//                        notifyDataSetChanged();
//                        checkIfNoItems();
//                    }
//                }
//                return true;
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mLinkView;
        public final TextView mDateView;
        public final ImageView mThumbView;
        public PhotoItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mLinkView = (TextView) view.findViewById(R.id.content_link);
            mDateView = (TextView) view.findViewById(R.id.date_time);
            mThumbView = (ImageView) view.findViewById(R.id.thumb_link);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLinkView.getText() + "'";
        }
    }
}
