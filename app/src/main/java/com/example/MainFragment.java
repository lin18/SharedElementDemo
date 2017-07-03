package com.example;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.example.ImageActivity.POSITION_KEY;

/**
 * Created by owp on 2017/6/29.
 */

public class MainFragment extends Fragment implements PictureAdapter.OnItemClickListener {

    ProgressBar mProgress;
    RecyclerView mRecyclerView;

    PictureAdapter adapter;

    List<Photo> photos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().postponeEnterTransition();
        // Listener to reset shared element exit transition callbacks.
        getActivity().getWindow().getSharedElementExitTransition().addListener(new TransitionCallback() {
            @Override
            public void onTransitionEnd(Transition transition) {
                SharedElementCallback callback = null;
                getActivity().setExitSharedElementCallback(callback);
            }
        });

        mProgress = (ProgressBar) getView().findViewById(R.id.progress);
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new PictureAdapter(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.addItemDecoration(new GridMarginDecoration(
                getResources().getDimensionPixelSize(R.dimen.grid_item_spacing)));

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected Void doInBackground(Void... params) {
                photos = new Gson().fromJson(Utils.readFromAssets(getActivity(), "test.json"), new TypeToken<ArrayList<Photo>>(){}.getType());
                return null;
            }
            @Override
            protected void onPostExecute(Void void1) {
                mProgress.setVisibility(View.GONE);
                adapter.setItems(photos);
            }
        }.execute();
    }

    public void onActivityReenter(int resultCode, Intent data) {
        getActivity().postponeEnterTransition();
        // Start the postponed transition when the recycler view is ready to be drawn.
        mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                getActivity().startPostponedEnterTransition();
                return true;
            }
        });

        if (data == null) {
            return;
        }

        final int selectedItem = data.getIntExtra(POSITION_KEY, 0);
        mRecyclerView.scrollToPosition(selectedItem);

        final PictureAdapter.ChildViewHolder holder = (PictureAdapter.ChildViewHolder)
                mRecyclerView.findViewHolderForAdapterPosition(selectedItem);
        if (holder == null) {
            Log.w("owp", "onActivityReenter: Holder is null, remapping cancelled.");
            return;
        }
        SharedElementEnterCallback callback =
                new SharedElementEnterCallback();
        callback.setView(holder.mIcon, holder.mTitle);
        getActivity().setExitSharedElementCallback(callback);
    }

    @Override
    public void onItemClick(View v, int position) {
//        final Photo photo = adapter.getItem(position);
//        final PictureAdapter.ChildViewHolder viewHolder = (PictureAdapter.ChildViewHolder)
//                mRecyclerView.findViewHolderForAdapterPosition(position);
        final ImageView mIcon = (ImageView) v.findViewById(android.R.id.icon);
        final TextView mTitle = (TextView) v.findViewById(android.R.id.title);
        ImageActivity.start(getActivity(), mIcon, mTitle, (ArrayList<Photo>) adapter.getItems(), position);
    }
}
