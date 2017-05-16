package com.app.easy_photo_to_link;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.app.easy_photo_to_link.dummy.PhotoContent.PhotoItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PhotoItem}
 */

public class MyPhotoHistoryRecyclerViewAdapter extends RecyclerView.Adapter<MyPhotoHistoryRecyclerViewAdapter.ViewHolder> {

    private final List<PhotoItem> mValues;
    private final Context mAppContext;

    public MyPhotoHistoryRecyclerViewAdapter(List<PhotoItem> items, Context listener) {
        mValues = items;
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
        holder.mCopyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) mAppContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Link copied!", holder.mItem.content_link);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mAppContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
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
