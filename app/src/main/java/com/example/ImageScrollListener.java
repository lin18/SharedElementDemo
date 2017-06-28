package com.example;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ImageScrollListener extends RecyclerView.OnScrollListener {
  private final Context context;

  public ImageScrollListener(Context context) {
    this.context = context;
  }

  @Override
  public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    super.onScrollStateChanged(recyclerView, newState);
    final Picasso picasso = Picasso.with(context);
    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
      picasso.resumeTag(context);
    } else {
      picasso.pauseTag(context);
    }
  }
}
