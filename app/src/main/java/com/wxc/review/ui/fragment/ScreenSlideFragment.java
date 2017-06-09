package com.wxc.review.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wxc.review.R;
import com.wxc.review.ui.activites.AppStartActivity;


public class ScreenSlideFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppStartActivity mainActivity = (AppStartActivity) getActivity();
        Bundle args = getArguments();
        int position = args.getInt("position");
        int layoutId = getLayoutId(position);
        ViewGroup rootView = (ViewGroup) inflater.inflate(layoutId, container, false);
        if (position == 0) {
            mainActivity.initFirstScreenViews(rootView, savedInstanceState);
        }
        if (position == 1) {

            mainActivity.initSecondScreenViews(rootView, savedInstanceState);
        }
        if (position == 2) {

            mainActivity.initThirdScreenViews(rootView, savedInstanceState);
        }

        return rootView;
    }

    /**
     * 获得三个引导页面的布局文件
     *
     * @param position
     * @return
     */
    private int getLayoutId(int position) {

        int id = 0;
        if (position == 0) {

            id = R.layout.first_screen;

        } else if (position == 1) {

            id = R.layout.second_screen;
        } else if (position == 2) {

            id = R.layout.third_screen;
        }
        return id;
    }


}
