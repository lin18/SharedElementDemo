package com.example;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class ImageActivity extends AppCompatActivity {

    public final static String PHOTOES_KEY = "photoes";
    public final static String POSITION_KEY = "position";

    ViewPager viewpager;

    List<Photo> photoes;
    int position;

    private SharedElementEnterCallback  sharedElementCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        postponeEnterTransition();


        TransitionSet transitions = new TransitionSet();
        Slide slide = new Slide(Gravity.BOTTOM);
        slide.setInterpolator(AnimationUtils.loadInterpolator(this,
                android.R.interpolator.linear_out_slow_in));
        slide.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
        transitions.addTransition(slide);
        transitions.addTransition(new Fade());
        getWindow().setEnterTransition(transitions);

        sharedElementCallback = new SharedElementEnterCallback();
        setEnterSharedElementCallback(sharedElementCallback);

        photoes = getIntent().getParcelableArrayListExtra(PHOTOES_KEY);
        position = getIntent().getIntExtra(POSITION_KEY, 0);


        viewpager = (ViewPager) findViewById(R.id.viewpager);


        viewpager.setAdapter(new ImagePagerAdapter(getSupportFragmentManager(), photoes, sharedElementCallback));
//        viewpager.setOffscreenPageLimit(1);
        viewpager.setCurrentItem(position);
        viewpager.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (viewpager.getChildCount() > 0) {
                    viewpager.removeOnLayoutChangeListener(this);
//                    if (Build.VERSION.SDK_INT >= 21) {
//                        startPostponedEnterTransition();
//                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        setActivityResult();
        super.onBackPressed();
    }

    @Override
    public void finishAfterTransition() {
        setActivityResult();
        super.finishAfterTransition();
    }

    private void setActivityResult() {
        if (position == viewpager.getCurrentItem()) {
            setResult(RESULT_OK);
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(POSITION_KEY, viewpager.getCurrentItem());
        setResult(RESULT_OK, intent);
    }

    public static void start(@NonNull Activity activity, @NonNull ImageView mIcon, @NonNull TextView mTitle, @NonNull ArrayList<Photo> photoes, int position) {
        final Intent intent = new Intent(activity, ImageActivity.class);

        intent.putExtra(PHOTOES_KEY, photoes);
        intent.putExtra(POSITION_KEY, position);

        Pair titlePair = Pair.create(mTitle, mTitle.getTransitionName());
        Pair iconPair = Pair.create(mIcon, mIcon.getTransitionName());

        View decorView = activity.getWindow().getDecorView();
        View statusBackground = decorView.findViewById(android.R.id.statusBarBackground);
        View navBackground = decorView.findViewById(android.R.id.navigationBarBackground);
        Pair statusPair = Pair.create(statusBackground,
                statusBackground.getTransitionName());

        final ActivityOptions options;
        if (navBackground == null) {
            options = ActivityOptions.makeSceneTransitionAnimation(activity,
                    titlePair, iconPair, statusPair);
        } else {
            Pair navPair = Pair.create(navBackground, navBackground.getTransitionName());
            options = ActivityOptions.makeSceneTransitionAnimation(activity,
                    titlePair, iconPair, statusPair, navPair);
        }
        activity.startActivity(intent, options.toBundle());
    }
}
