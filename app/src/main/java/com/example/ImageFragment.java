package com.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.EmptySignature;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import uk.co.senab.photoview.PhotoView;

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

    ProgressBar progress;
    PhotoView mIcon;
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
        progress = (ProgressBar) getView().findViewById(R.id.progress);
        mIcon = (PhotoView) getView().findViewById(android.R.id.icon);
        mTitle = (TextView) getView().findViewById(android.R.id.title);

        mIcon.setScaleType(ImageView.ScaleType.CENTER);

        photo = getArguments().getParcelable(PHOTO_KEY);
        final int requestedPhotoWidth = getResources().getDisplayMetrics().widthPixels;
        mTitle.setText(photo.author);
        mTitle.setTransitionName(photo.id + "");
        mIcon.setTransitionName(photo.author);

        if (isExists(photo.getPhotoUrl(750))) {
            Glide.with(this)
                    .load(photo.getPhotoUrl(750))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(setListener())
                    .into(setTarget());
        } else {
            progress.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(photo.getPhotoUrl(750))
                    .thumbnail(Glide.with(this)
                            .load(photo.getPhotoUrl(160))
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(setListener())
                    .into(setTarget());
        }
//        Picasso.with(getActivity())
//                .load(photo.getPhotoUrl(1080))
//                .into(mIcon, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        startPostponedEnterTransition();
//                    }
//
//                    @Override
//                    public void onError() {
//                        startPostponedEnterTransition();
//                    }
//                });

    }

    @NonNull
    private RequestListener<String, GlideDrawable> setListener() {
        return new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                progress.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "加载高清图片失败", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progress.setVisibility(View.GONE);
                mIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return false;
            }
        };
    }

    @NonNull
    private SimpleTarget<GlideDrawable> setTarget() {
        return new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                mIcon.setImageDrawable(resource);
                getActivity().startPostponedEnterTransition();
            }
        };
    }

    /**
     * 判断是否存在缓存
     */
    public boolean isExists(String url) {
        try {
            Class claz = Class.forName("com.bumptech.glide.load.engine.OriginalKey");
            Constructor constructor = claz.getConstructor(String.class, Key.class);
            constructor.setAccessible(true);
            File file = DiskLruCacheWrapper.get(Glide.getPhotoCacheDir(getActivity()), 1024)
                    .get((Key) constructor.newInstance(url, EmptySignature.obtain()));
            constructor.setAccessible(false);
            return file != null && file.exists();
        } catch (ClassNotFoundException | NoSuchMethodException | java.lang.InstantiationException
                | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ImageView getIcon() {
        return mIcon;
    }

    public TextView getTitle() {
        return mTitle;
    }
}
