package com.app.easy_photo_to_link;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.app.easy_photo_to_link.dummy.PhotoContent.PhotoItem;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.Resources;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PhotoItem}
 */

public class MyPhotoHistoryRecyclerViewAdapter extends RecyclerView.Adapter<MyPhotoHistoryRecyclerViewAdapter.ViewHolder> {

    public final List<PhotoItem> mValues;
    private final Context mAppContext;
    private final OnChangeItemListener changeItemListener;
    public List<ViewHolder> selectedItems;

    public MyPhotoHistoryRecyclerViewAdapter(List<PhotoItem> items, Context listener, OnChangeItemListener changeItemListener) {
        this.mValues = items;
        this.mAppContext = listener;
        this.changeItemListener = changeItemListener;
        this.selectedItems = new ArrayList<>();
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
        setColorForView((GradientDrawable) holder.mView.getBackground(), R.color.colorWhite);
        Picasso.with(mAppContext)
                .load(mValues.get(position).thumb_link)
                .placeholder(R.mipmap.image_loading)
                .error(R.mipmap.image_error)
                .into(holder.mThumbView);
        holder.mCopyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                execCopyText(holder);
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                execSelectItem(holder);
            }
        });
    }

    private void execCopyText(ViewHolder holder)
    {
        ClipboardManager clipboard = (ClipboardManager) mAppContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Link copied!", holder.mItem.content_link);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(mAppContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }
    private void execSelectItem(ViewHolder holder)
    {
        GradientDrawable drawable = (GradientDrawable) holder.mView.getBackground();
        if (!holder.isSelected) {
            setColorForView(drawable, R.color.colorAccent);
            selectedItems.add(holder);
        }
        else
        {
            setColorForView(drawable, R.color.colorWhite);
            selectedItems.remove(holder);
        }
        holder.isSelected = !holder.isSelected;
        changeItemListener.onChangeItem(selectedItems);
    }
    private void setColorForView(GradientDrawable drawable, int colorResId)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawable.setColor(mAppContext.getColor(colorResId));
        } else {
            drawable.setColor(mAppContext.getResources().getColor(colorResId));
        }
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
        public boolean isSelected = false;

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
    public interface OnChangeItemListener
    {
        void onChangeItem(List<ViewHolder> viewHolder);
    }
}
