package com.example;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 *
 * Created by owp on 2017/4/10.
 */

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ChildViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    @Nullable
    public OnItemClickListener mItemClickListener;
    @NonNull
    private List<Photo> items;

    public PictureAdapter(@Nullable Object listener) {
        addListener(listener);
    }

    public void addListener(@Nullable Object listener) {
        if (listener instanceof OnItemClickListener) {
            mItemClickListener = (OnItemClickListener) listener;
        }
    }

    public void setItems(@NonNull List<Photo> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    public List<Photo> getItems() {
        return items;
    }

    public Photo getItem(@IntRange(from = 0) int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChildViewHolder(parent, R.layout.picture_item);
    }

    @Override
    public void onBindViewHolder(final ChildViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();
        final int requestedPhotoWidth = context.getResources().getDisplayMetrics().widthPixels;
        Photo photo = getItem(position);
        holder.mTitle.setText(photo.author + "");
        holder.mTitle.setTransitionName(photo.id + "");
        holder.mIcon.setTransitionName(photo.author);
        Glide.with(context)
                .load(photo.getPhotoUrl(160))
                .placeholder(R.drawable.ic_image)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.mIcon);
//        Picasso.with(context)
//                .load(photo.getPhotoUrl(160))
//                .into(holder.mIcon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });
    }

    static class ChildViewHolder extends RecyclerView.ViewHolder {

        ImageView mIcon;
        TextView mTitle;

        ChildViewHolder(@NonNull ViewGroup parent, @LayoutRes int layoutResId) {
            this(LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false));
        }

        ChildViewHolder(View itemView) {
            super(itemView);
            this.mIcon = (ImageView) itemView.findViewById(android.R.id.icon);
            this.mTitle = (TextView) itemView.findViewById(android.R.id.title);
        }
    }
}
