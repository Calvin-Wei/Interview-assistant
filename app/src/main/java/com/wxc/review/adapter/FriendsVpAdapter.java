package com.wxc.review.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wxc.review.ui.fragment.FriendsVpFragment;

/**
 * Created by wxc on 2017/5/5.
 */

public class FriendsVpAdapter extends FragmentPagerAdapter {
    public FriendsVpAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position==0) {
            return new FriendsVpFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
