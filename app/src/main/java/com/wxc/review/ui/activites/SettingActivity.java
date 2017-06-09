package com.wxc.review.ui.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wxc.review.R;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private View mRootView;
    private RelativeLayout mBtSuggest, mBtUpdate, mBtAbout, mBtFav, mBtSettingCache, mBtCreateTable;
    private ImageView icon_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    public void initView() {
        icon_rl = (ImageView) findViewById(R.id.rl_icon);
        mBtSettingCache = (RelativeLayout) findViewById(R.id.bt_cache);
        mBtSuggest = (RelativeLayout) findViewById(R.id.bt_suggest);
        mBtUpdate = (RelativeLayout) findViewById(R.id.bt_update);
        mBtAbout = (RelativeLayout) findViewById(R.id.bt_about);
        mBtCreateTable = (RelativeLayout) findViewById(R.id.bt_create_table);

        icon_rl.setOnClickListener(this);
        mBtSuggest.setOnClickListener(this);
        mBtCreateTable.setOnClickListener(this);
        mBtUpdate.setOnClickListener(this);
        mBtSettingCache.setOnClickListener(this);
        mBtAbout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rl_icon:
                finish();
                break;
            case R.id.bt_cache:
                intent.setClass(this, SettingCacheActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_suggest:
                intent.setClass(this, SuggestActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_update:
                break;
            case R.id.bt_about:
                intent.setClass(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_create_table:
                intent.setClass(this, CreateTableActivity.class);
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
