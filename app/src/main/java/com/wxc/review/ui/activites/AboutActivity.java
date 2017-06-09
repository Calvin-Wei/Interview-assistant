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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wxc.review.R;
import com.wxc.review.base.BaseActivity;


public class AboutActivity extends BaseActivity {

    private TextView mBtAuthor,mBtUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initToolBar();
        showOrHideToolBarNavigation(true);

        mBtAuthor = (TextView) findViewById(R.id.bt_about_author);
        mBtAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this,AuthorActivity.class);
                startActivity(intent);
            }
        });

        mBtUpdate = (TextView) findViewById(R.id.bt_update_text);
        mBtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this,VersionNoteActivity.class);
                startActivity(intent);
            }
        });

        setStatusBarCompat();
    }

    @Override
    public String returnToolBarTitle() {
        return getString(R.string.about);
    }
}
