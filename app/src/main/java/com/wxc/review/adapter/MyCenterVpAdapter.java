package com.wxc.review.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.BaseAdapter;

import com.wxc.review.ui.fragment.GuanZhuFriendsFragment;
import com.wxc.review.ui.fragment.MyInvitationFragment;

import java.util.List;

/**
 * Created by wxc on 2017/5/5.
 */

public class MyCenterVpAdapter extends FragmentPagerAdapter {
    private List<String> list;

    public MyCenterVpAdapter(FragmentManager fm, List<String> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return MyInvitationFragment.getInstance();
            case 1:
                return new GuanZhuFriendsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    //标题方法，要重写
    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).toString();
    }
}
