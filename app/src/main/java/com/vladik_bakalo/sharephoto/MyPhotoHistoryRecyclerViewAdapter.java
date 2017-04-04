package com.vladik_bakalo.sharephoto;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vladik_bakalo.sharephoto.dummy.PhotoContent.PhotoItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PhotoItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */

public class MyPhotoHistoryRecyclerViewAdapter extends RecyclerView.Adapter<MyPhotoHistoryRecyclerViewAdapter.ViewHolder> {

    private final List<PhotoItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Context mAppContext;

    public MyPhotoHistoryRecyclerViewAdapter(List<PhotoItem> items, Context listener) {
        mValues = items;
        mListener = (OnListFragmentInteractionListener) listener;
        mAppContext = listener;
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
        Picasso.with(mAppContext)
                .load(mValues.get(position).thumb_link)
                .placeholder(R.mipmap.image_loading)
                .error(R.mipmap.image_error)
                .into(holder.mThumbView);

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
        holder.mCopyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        public final ImageView mCopyView;
        public PhotoItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mLinkView = (TextView) view.findViewById(R.id.content_link);
            mDateView = (TextView) view.findViewById(R.id.date_time);
            mThumbView = (ImageView) view.findViewById(R.id.thumb_link);
            mCopyView = (ImageView) view.findViewById(R.id.image_copy_to_clipboard);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLinkView.getText() + "'";
        }
    }
}
