package com.example;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.FileDescriptorLocalUriFetcher;
import com.bumptech.glide.load.data.HttpUrlFetcher;
import com.bumptech.glide.load.model.GlideUrl;

public class MainActivity extends AppCompatActivity {

    MainFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Glide.getPhotoCacheDir(this);
        new HttpUrlFetcher(new GlideUrl("https://unsplash.it/160?image=350")).getId();
        fragment = new MainFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_layout, fragment, "fragment").commit();
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        fragment.onActivityReenter(resultCode, data);
    }

}
