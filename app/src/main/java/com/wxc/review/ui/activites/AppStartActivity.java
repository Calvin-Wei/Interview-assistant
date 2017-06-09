/*
 * Copyright (c) 2016. Vv <envyfan@qq.com><http://www.v-sounds.com/>
 *
 * This file is part of AndroidReview (Android面试复习)
 *
 * AndroidReview is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  AndroidReview is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 * along with AndroidReview.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.wxc.review.ui.activites;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wxc.review.R;
import com.wxc.review.base.system.AppManager;
import com.wxc.review.ui.fragment.ScreenSlideFragment;
import com.wxc.review.ui.view.BookView;
import com.wxc.review.ui.view.SunMoonView;
import com.wxc.review.ui.view.ThirdScreenView;
import com.wxc.review.utils.PermissionsChecker;

import java.util.HashMap;


public class AppStartActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private LinearLayout mIndicatorLayout;
    private TextView mIndicatorView[];
    private Drawable mPagerBackground;


    //第一个引导页上面的图像
    private ImageView mCenterBox;
    private ImageView mRenRenImage;
    private ImageView mWangYiImage;
    private ImageView mIqiYiImage;
    private ImageView mSinaImage;
    private ImageView mTaoBaoImage;
    private ImageView mMiImage;
    private ImageView mJingDongImage;
    private ImageView mYouKuImage;
    //
    private AnimatorSet mAnimatorSet;

    private TextView mTitleText;
    private TextView mDescText;


    private boolean mSecondPageSelected;
    private HashMap<ImageView, Float> mOriginalXValuesMap = new HashMap<>();
    private int mSelectedPosition = -1;

    //第二个引导页
    private SunMoonView mAnimationView;
    private float mPreviousPositionOffset;
    private boolean mViewPagerScrollingLeft;
    private int mPreviousPosition;
    private BookView mBookView;


    //第三个引导页
    private boolean mShouldSpheresRotate = true;
    private ThirdScreenView mRoundView;
    private boolean mThirdPageSelected;
    private Button mLetsGoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_start);

        setUpViews();


    }

    private void setUpViews() {

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerBackground = mPager.getBackground();
        mIndicatorLayout = (LinearLayout) findViewById(R.id.indicator_layout);

        mPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));
        setIndicatorLayout();
        setPageChangeListener(mPager);
        mPager.bringToFront();
        //mPagerBackground.setAlpha(0);

        //mPager.setOffscreenPageLimit(2);

    }

    /**
     * 设置三个圆圈，引导页的标记位置
     */
    private void setIndicatorLayout() {

        int dotsCount = NUM_PAGES;
        mIndicatorView = new TextView[dotsCount];
        for (int i = 0; i < dotsCount; i++) {

            mIndicatorView[i] = new TextView(this);
            mIndicatorView[i].setWidth((int) getResources().getDimension(R.dimen.dimen_12));
            mIndicatorView[i].setHeight((int) getResources().getDimension(R.dimen.dimen_12));
            mIndicatorView[i].setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, (int) getResources().getDimension(R.dimen.dimen_15), 0);
            mIndicatorView[i].setLayoutParams(params);
            mIndicatorView[i].setBackgroundResource(R.drawable.rounded_cell_gray);
            mIndicatorLayout.addView(mIndicatorView[i]);

        }

        //mIndicatorView[0].setWidth(20);
        //mIndicatorView[0].setHeight(20);
        mIndicatorView[0].setBackgroundResource(R.drawable.rounded_cell_red);
        mIndicatorView[0].setGravity(Gravity.CENTER);
    }

    /**
     * ViewPager的滑动监听
     *
     * @param viewPager
     */
    private void setPageChangeListener(ViewPager viewPager) {


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * 页面滑动
             * @param position 位置
             * @param positionOffset 滑动前
             * @param positionOffsetPixels 滑动后
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                // 左右滑动判断
                if ((positionOffset > mPreviousPositionOffset && position == mPreviousPosition) || (positionOffset < mPreviousPositionOffset && position > mPreviousPosition)) {
                    mViewPagerScrollingLeft = true;
                } else if (positionOffset < mPreviousPositionOffset) {

                    mViewPagerScrollingLeft = false;
                }
                mPreviousPositionOffset = positionOffset;
                mPreviousPosition = position;

                // 透明度变化
                if (position == 1 && mViewPagerScrollingLeft) {

                    mIndicatorLayout.setAlpha(1 - positionOffset);
                } else if (position == 1 && !mViewPagerScrollingLeft) {

                    mIndicatorLayout.setAlpha(1 - positionOffset);
                }

            }

            /**
             * 当前选择的页面位置
             * @param position
             */
            @Override
            public void onPageSelected(int position) {
                //加载第二个页面的动画
                if (position == 1) {
                    mSelectedPosition = 1;
                    mSecondPageSelected = true;
                    setViewsInOriginalPosition();
                    //initializeAlpha();
                    if (mAnimatorSet != null) {
                        mAnimatorSet.cancel();
                    }

                    animateBookView();
                }
                if (position == 0) {
                    mSelectedPosition = 0;
                    doFadeAnimation();

                }

                //三个小圆圈(用来标记引导页的位置)
                for (int i = 0; i < mIndicatorView.length; i++) {
                    mIndicatorView[i].setBackgroundResource(R.drawable.rounded_cell_gray);
                }
                mIndicatorView[position].setBackgroundResource(R.drawable.rounded_cell_red);
            }


            //滑动改变
            @Override
            public void onPageScrollStateChanged(int state) {
                //正在拖动则旋转取消(第三个引导页)
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    mShouldSpheresRotate = false;
                } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mShouldSpheresRotate = true;
                }
                if (mRoundView != null) {
                    mRoundView.setRotatingPermission(mShouldSpheresRotate);
                }

                if (mSelectedPosition == 0 && state == ViewPager.SCROLL_STATE_IDLE) {
                    mSecondPageSelected = false;
                }

            }
        });

    }

    /**
     * 第二个引导页,渐进依次加载
     */
    private void animateBookView() {

        mBookView.fadeInTheLines();
    }

    /**
     * 图像显示位置
     */
    private void setViewsInOriginalPosition() {

        mCenterBox.setX(mOriginalXValuesMap.get(mCenterBox));
        mRenRenImage.setX(mOriginalXValuesMap.get(mRenRenImage));
        mWangYiImage.setX(mOriginalXValuesMap.get(mWangYiImage));
        mIqiYiImage.setX(mOriginalXValuesMap.get(mIqiYiImage));
        mSinaImage.setX(mOriginalXValuesMap.get(mSinaImage));
        mTaoBaoImage.setX(mOriginalXValuesMap.get(mTaoBaoImage));
        mMiImage.setX(mOriginalXValuesMap.get(mMiImage));
        mJingDongImage.setX(mOriginalXValuesMap.get(mJingDongImage));

        mYouKuImage.setX(mOriginalXValuesMap.get(mYouKuImage));

        initializeAlpha();

    }

    /**
     * 每个引导页都是fragment
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            ScreenSlideFragment fragment = new ScreenSlideFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_setting) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * viewPager平移滑动
     */
    private class CustomTransformer implements ViewPager.PageTransformer {


        /**
         * 根据滑动方向和当前位置来加载
         *
         * @param page     当前页面视图
         * @param position 当前位置
         */
        @Override
        public void transformPage(View page, float position) {

            int pageWidth = page.getWidth();
            if ((mViewPagerScrollingLeft && page.findViewById(R.id.center_box) != null)) {
                animateSecondScreen(position, pageWidth, 0);
            }

            if (!mViewPagerScrollingLeft && page.findViewById(R.id.center_box_second) != null) {
                animateSecondScreen(position, pageWidth, 1);
            }

            if (position < -1) {

            } else if (position <= 1) {

                if (!mSecondPageSelected && page.findViewById(R.id.center_box_second) != null) {
                    moveTheSpheres(position, pageWidth);
                }

                if (!mShouldSpheresRotate && page.findViewById(R.id.center_box_third) != null) {
                    mRoundView.translateTheSpheres(position, pageWidth);
                }


            } else {

            }

        }
    }

    /**
     * 第一个引导页，滑动切换时，图像进行偏移移动
     *
     * @param position
     * @param pageWidth 页面宽度
     */
    private void moveTheSpheres(float position, int pageWidth) {


        float camcordPos = (float) ((1 - position) * 0.15 * pageWidth);
        if (camcordPos > (-1 * mOriginalXValuesMap.get(mRenRenImage))) {
            mRenRenImage.setTranslationX(camcordPos);
        }


        float clockPos = (float) ((1 - position) * 0.50 * pageWidth);
        if (clockPos > (-1 * mOriginalXValuesMap.get(mWangYiImage))) {
            mWangYiImage.setTranslationX(clockPos);
        }

        float graphPos = (float) ((1 - position) * 0.50 * pageWidth);
        if (graphPos > (-1 * mOriginalXValuesMap.get(mIqiYiImage))) {
            mIqiYiImage.setTranslationX(graphPos);
        }

        float audioPos = (float) ((1 - position) * 0.30 * pageWidth);
        if (audioPos > (-1 * mOriginalXValuesMap.get(mSinaImage))) {
            mSinaImage.setTranslationX(audioPos);
        }


        float quotePos = (float) (-(1 - position) * 0.37 * pageWidth);
        if (quotePos > (-1 * mOriginalXValuesMap.get(mTaoBaoImage))) {
            mTaoBaoImage.setTranslationX(quotePos);
        }

        float mapPos = (float) (-(1 - position) * 1.1 * pageWidth);
        if (mapPos > (-1 * mOriginalXValuesMap.get(mMiImage))) {
            mMiImage.setTranslationX(mapPos);
        }

        float wordpressPos = (float) (-(1 - position) * 0.37 * pageWidth);
        if (wordpressPos > (-1 * mOriginalXValuesMap.get(mJingDongImage))) {
            mJingDongImage.setTranslationX(wordpressPos);
        }


    }

    /**
     * 第二个引导页，顺时针和逆时针动画的设置
     *
     * @param position
     * @param pageWidth
     * @param direction
     */
    private void animateSecondScreen(float position, int pageWidth, int direction) {

        if (direction == 0) {
            mAnimationView.animateSecondScreenClock(position);
        } else {
            mAnimationView.animateSecondScreenAntiClock(position);
        }
    }


    /**
     * 第一个引导页使用主线程Handler进行延迟加载(700毫秒)
     *
     * @param rootView
     * @param savedInstanceState
     */
    public void initFirstScreenViews(View rootView, final Bundle savedInstanceState) {

        mCenterBox = (ImageView) rootView.findViewById(R.id.center_box);
        mRenRenImage = (ImageView) rootView.findViewById(R.id.renren);
        mWangYiImage = (ImageView) rootView.findViewById(R.id.wangyi);
        mIqiYiImage = (ImageView) rootView.findViewById(R.id.iqiyi);
        mSinaImage = (ImageView) rootView.findViewById(R.id.sina);
        mTaoBaoImage = (ImageView) rootView.findViewById(R.id.taobao);
        mMiImage = (ImageView) rootView.findViewById(R.id.mi);
        mJingDongImage = (ImageView) rootView.findViewById(R.id.jingdong);
        mYouKuImage = (ImageView) rootView.findViewById(R.id.youku);
        initializeAlpha();

        rootView.post(new Runnable() {
            @Override
            public void run() {

                getOriginalXValues(savedInstanceState);

            }
        });

        if (savedInstanceState == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    doFadeAnimation();
                }
            }, 700);

        }

    }

    /**
     * X轴的偏移量
     *
     * @param savedInstanceState
     */
    private void getOriginalXValues(Bundle savedInstanceState) {

        mOriginalXValuesMap.put(mCenterBox, mCenterBox.getX());
        mOriginalXValuesMap.put(mRenRenImage, mRenRenImage.getX());
        mOriginalXValuesMap.put(mWangYiImage, mWangYiImage.getX());
        mOriginalXValuesMap.put(mIqiYiImage, mIqiYiImage.getX());
        mOriginalXValuesMap.put(mSinaImage, mSinaImage.getX());
        mOriginalXValuesMap.put(mTaoBaoImage, mTaoBaoImage.getX());
        mOriginalXValuesMap.put(mMiImage, mMiImage.getX());
        mOriginalXValuesMap.put(mJingDongImage, mJingDongImage.getX());
        mOriginalXValuesMap.put(mYouKuImage, mYouKuImage.getX());
        if (savedInstanceState == null) {
            mPager.setPageTransformer(true, new CustomTransformer());
        }


    }

    /**
     * 透明度初始化(第一个界面的图像)
     */
    private void initializeAlpha() {

        mRenRenImage.setAlpha(0f);
        mWangYiImage.setAlpha(0f);
        mIqiYiImage.setAlpha(0f);
        mSinaImage.setAlpha(0f);
        mTaoBaoImage.setAlpha(0f);
        mMiImage.setAlpha(0f);
        mJingDongImage.setAlpha(0f);
        mYouKuImage.setAlpha(0f);
    }

    /**
     * 图标渐进动画(第一个引导页)
     * 持续时间为700毫秒，
     * 透明度从0-1
     * 并设置每个图标的延迟加载时间
     */
    private void doFadeAnimation() {


        ObjectAnimator fadeCamcord = ObjectAnimator.ofFloat(mRenRenImage, "alpha", 0f, 1f);
        fadeCamcord.setDuration(700);

        ObjectAnimator fadeClock = ObjectAnimator.ofFloat(mWangYiImage, "alpha", 0f, 1f);
        fadeClock.setDuration(700);

        ObjectAnimator fadeGraph = ObjectAnimator.ofFloat(mIqiYiImage, "alpha", 0f, 1f);
        fadeGraph.setDuration(700);

        ObjectAnimator fadeAudio = ObjectAnimator.ofFloat(mSinaImage, "alpha", 0f, 1f);
        fadeAudio.setDuration(700);

        ObjectAnimator fadeQuote = ObjectAnimator.ofFloat(mTaoBaoImage, "alpha", 0f, 1f);
        fadeQuote.setDuration(700);

        ObjectAnimator fadeMap = ObjectAnimator.ofFloat(mMiImage, "alpha", 0f, 1f);
        fadeMap.setDuration(700);

        ObjectAnimator fadeWordpress = ObjectAnimator.ofFloat(mJingDongImage, "alpha", 0f, 1f);
        fadeWordpress.setDuration(700);

        ObjectAnimator fadeyouku = ObjectAnimator.ofFloat(mYouKuImage, "alpha", 0f, 1f);
        fadeWordpress.setDuration(700);

        //1 5    3 2  7 6  4

        mAnimatorSet = new AnimatorSet();
        fadeAudio.setStartDelay(50);
        fadeGraph.setStartDelay(200);
        fadeWordpress.setStartDelay(500);
        fadeClock.setStartDelay(700);
        fadeMap.setStartDelay(900);
        fadeQuote.setStartDelay(1100);
        fadeyouku.setStartDelay(800);

        mAnimatorSet.play(fadeCamcord).with(fadeAudio).with(fadeGraph).with(fadeWordpress).with(fadeClock).with(fadeMap).with(fadeQuote).with(fadeyouku);
        mAnimatorSet.start();

    }

    /**
     * 第二个引导页布局加载
     *
     * @param rootView
     * @param savedInstanceState
     */
    public void initSecondScreenViews(View rootView, Bundle savedInstanceState) {

        final RelativeLayout secondScreenRoot = (RelativeLayout) rootView.findViewById(R.id.root);
        //final ImageView centerBox=(ImageView)rootView.findViewById(R.id.center_box_second);
        mBookView = (BookView) rootView.findViewById(R.id.center_box_second);
        mAnimationView = (SunMoonView) rootView.findViewById(R.id.animation_view);


    }

    /**
     * 第三个引导页加载初始化
     *
     * @param rootView
     * @param savedInstanceState
     */
    public void initThirdScreenViews(View rootView, Bundle savedInstanceState) {

        mRoundView = (ThirdScreenView) rootView.findViewById(R.id.round_view);
        mLetsGoButton = (Button) rootView.findViewById(R.id.letsgo);

        mLetsGoButton.setOnClickListener(clickListener);
        mRoundView.setContext(this);

    }

    /**
     * 按钮监听
     */
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.letsgo:

                    mRoundView.startNextScreen();

                    break;
            }
        }
    };


}
