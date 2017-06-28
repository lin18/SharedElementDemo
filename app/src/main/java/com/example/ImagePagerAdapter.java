package com.example;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by owp on 2017/6/27.
 */

public class ImagePagerAdapter extends FragmentStatePagerAdapter {

    List<Photo> photoes;
    SharedElementEnterCallback  sharedElementCallback;

    public ImagePagerAdapter(FragmentManager fm, List<Photo> photoes,
                             SharedElementEnterCallback  sharedElementCallback) {
        super(fm);
        this.photoes = photoes;
        this.sharedElementCallback = sharedElementCallback;
    }

    @Override
    public int getCount() {
        return photoes.size();
    }

    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(photoes.get(position));
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (object instanceof ImageFragment) {
            ImageFragment fragment = (ImageFragment) object;
            sharedElementCallback.setView(fragment.getIcon(), fragment.getTitle());
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
