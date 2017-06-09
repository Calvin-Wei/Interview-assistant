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

package com.wxc.review.ui.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.wxc.review.R;
import com.wxc.review.base.BaseFragment;
import com.wxc.review.entity.User;
import com.wxc.review.ui.activites.AboutActivity;
import com.wxc.review.ui.activites.CreateTableActivity;
import com.wxc.review.ui.activites.ListActivity;
import com.wxc.review.ui.activites.LunTanActivity;
import com.wxc.review.ui.activites.MyCenterActivity;
import com.wxc.review.ui.activites.SettingActivity;
import com.wxc.review.ui.activites.SettingCacheActivity;
import com.wxc.review.ui.activites.SuggestActivity;
import com.wxc.review.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobUser;


/**
 * Author：wxc on .
 * Mail：519462367@qq.com
 * Description：个人信息设置
 */
public class SettingFragment extends BaseFragment {
    private View mRootView;
    private RelativeLayout mBtLuntan, mBtMyCenter, mBtSetting, mBtFav, mBtExit;
    private int icon_id = 0;
    private CircularImageView user_head;
    private TextView userNickName;
    private TextView userMotto;
    private Picasso picasso;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        super.onResume();
        //获取登录后的头像
        User bmobUser = BmobUser.getCurrentUser(getContext(), User.class);//获取缓存用户
        if (bmobUser != null) {
            //6.0
            if (Build.VERSION.SDK_INT >= 23) {
                Log.e("TAG",bmobUser.getNickname()+" "+bmobUser.getUsername()+" "+bmobUser.getUserId());
                Utils.drawable_icon=bmobUser.getType();
                icon_id = Utils.drawable_icon;
//        自V3.4.5版本开始，SDK新增了getObjectByKey(context,key)方法从本地缓存中获取当前登陆用户某一列的值。其中key为用户表的指定列名。
                JSONObject json = (JSONObject) BmobUser.getObjectByKey(getContext(), "icon");
                if (json == null) {
                    Log.e("TAG",icon_id+" ");
                    user_head.setImageDrawable(getResources().getDrawable(icon_id));
                    userNickName.setText(bmobUser.getNickname());
                    userMotto.setText(bmobUser.getMotto());
                } else {  //修改头像后 icon不为null
                    try {
                        String icon_url = json.getString("url");
//                        Picasso.with(getContext()).load(icon_url).into(mycion);
                        Picasso.with(getContext()).load(icon_url).into(user_head);
                        userNickName.setText(bmobUser.getNickname());
                        userMotto.setText(bmobUser.getMotto());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }

            //system<6.0
            JSONObject json = (JSONObject) BmobUser.getObjectByKey(getContext(), "icon");//或 bmobUser.getIcon().getUrl()
            //6.0以下的手机系统登录 6.0注册的账号(头像未修改时 bmob端的icon字段为null)
            if (json == null) {
                if (bmobUser.getType() == 0) {
                    picasso = Picasso.with(getContext());
                    picasso.load(bmobUser.getIcon().getUrl()).into(user_head);
                }
                user_head.setImageDrawable(getResources().getDrawable(bmobUser.getType()));
                userNickName.setText(bmobUser.getNickname());
                userMotto.setText(bmobUser.getMotto());
                return;
            }
            try {
                String icon_url = json.getString("url");
                Picasso.with(getContext()).load(icon_url).into(user_head);
                userNickName.setText(bmobUser.getNickname());
                userMotto.setText(bmobUser.getMotto());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            //未登陆头像
            user_head.setImageDrawable(getResources().getDrawable(R.mipmap.icon));
            userNickName.setText("点击头像登陆");
            userMotto.setText("***我的个性签名***");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_info, container, false);
            creatViews();
        }
        //缓存的mRootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个mRootView已经有parent的错误。
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;

    }

    private void creatViews() {
        mBtLuntan = (RelativeLayout) mRootView.findViewById(R.id.luntan);
        mBtMyCenter = (RelativeLayout) mRootView.findViewById(R.id.bt_mycenter);
        mBtSetting = (RelativeLayout) mRootView.findViewById(R.id.bt_setting);
        mBtFav = (RelativeLayout) mRootView.findViewById(R.id.bt_my_fav);
        mBtExit = (RelativeLayout) mRootView.findViewById(R.id.bt_exit);

        user_head = (CircularImageView) mRootView.findViewById(R.id.user_head);
        userMotto= (TextView) mRootView.findViewById(R.id.user_motto);

        userNickName= (TextView) mRootView.findViewById(R.id.user_nickname);

        BtClickListener clickListener = new BtClickListener();
        mBtLuntan.setOnClickListener(clickListener);
        mBtMyCenter.setOnClickListener(clickListener);
        mBtSetting.setOnClickListener(clickListener);
        mBtFav.setOnClickListener(clickListener);
        mBtExit.setOnClickListener(clickListener);
        user_head.setOnClickListener(clickListener);

    }

    private class BtClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.luntan:
                    intent.setClass(getContext(), LunTanActivity.class);
                    startActivity(intent);
                    break;
                case R.id.bt_mycenter:
                    intent.setClass(getContext(),MyCenterActivity.class);
                    startActivity(intent);
                    break;
                case R.id.bt_setting:
                    intent.setClass(getContext(), SettingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.bt_my_fav:
                    intent.setClass(getContext(), ListActivity.class);
                    intent.putExtra(ListActivity.CONTENT_TYPE_KEY, ListActivity.LIST_TYPE_FAV_TEST);
                    startActivity(intent);
                    break;
                case R.id.bt_exit:
                    System.exit(0);
                    break;
                case R.id.user_head:
                    intent.setClass(getContext(), MyCenterActivity.class);
                    startActivity(intent);
                    break;
            }
//            switch (v.getId()) {
//                case R.id.bt_cache:
//                    intent.setClass(getContext(), SettingCacheActivity.class);
//                    startActivity(intent);
//                    break;
//                case R.id.bt_suggest:
//                    intent.setClass(getContext(), SuggestActivity.class);
//                    startActivity(intent);
//                    break;
//                case R.id.bt_update:
//                    break;
//                case R.id.bt_about:
//                    intent.setClass(getContext(), AboutActivity.class);
//                    startActivity(intent);
//                    break;
//                case R.id.bt_my_fav:
//                    intent.setClass(getContext(), ListActivity.class);
//                    intent.putExtra(ListActivity.CONTENT_TYPE_KEY, ListActivity.LIST_TYPE_FAV_TEST);
//                    startActivity(intent);
//                    break;
//                case R.id.bt_create_table:
//                    intent.setClass(getContext(), CreateTableActivity.class);
//                    startActivity(intent);
//                    break;
//            }
        }
    }
}
