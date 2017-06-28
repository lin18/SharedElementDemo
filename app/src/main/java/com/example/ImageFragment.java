package com.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by owp on 2017/6/27.
 */

public class ImageFragment extends Fragment {

    public final static String PHOTO_KEY = "photo";

    public static ImageFragment newInstance(Photo photo) {
        ImageFragment fragment = new ImageFragment();

        Bundle args = new Bundle();
        args.putParcelable(PHOTO_KEY, photo);
        fragment.setArguments(args);

        return fragment;
    }

    ImageView mIcon;
    TextView mTitle;
    Photo photo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIcon = (ImageView) getView().findViewById(android.R.id.icon);
        mTitle = (TextView) getView().findViewById(android.R.id.title);

        photo = getArguments().getParcelable(PHOTO_KEY);
        final int requestedPhotoWidth = getResources().getDisplayMetrics().widthPixels;
        mTitle.setText(photo.author);
        mTitle.setTransitionName(photo.id + "");
        mIcon.setTransitionName(photo.author);
        Picasso.with(getActivity())
                .load(photo.getPhotoUrl(requestedPhotoWidth))
                .into(mIcon, new Callback() {
                    @Override
                    public void onSuccess() {
                        startPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {
                        startPostponedEnterTransition();
                    }
                });

    }

    public ImageView getIcon() {
        return mIcon;
    }

    public TextView getTitle() {
        return mTitle;
    }
}
