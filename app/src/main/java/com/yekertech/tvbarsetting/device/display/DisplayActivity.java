/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yekertech.tvbarsetting.device.display;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.util.DisplayMetrics;

import com.yekertech.tvbarsetting.BrowseInfoFactory;
import com.yekertech.tvbarsetting.R;

import java.net.URI;
import java.util.Timer;

/**
 * Activity allowing the management of display settings.
 */
public class DisplayActivity extends Activity {

    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private URI mBackgroundURI;
    private BackgroundManager mBackgroundManager;


    protected String getBrowseTitle() {
        return getString(R.string.device_display);
    }


    protected Drawable getBadgeImage() {
        return getResources().getDrawable(R.drawable.ic_settings_display);
    }

    protected BrowseInfoFactory getBrowseInfoFactory() {
        DisplayBrowseInfo displayBrowseInfo = new DisplayBrowseInfo(this);
        displayBrowseInfo.init();
        return displayBrowseInfo;
    }

    private static final String TAG_BROWSE_FRAGMENT = "browse_fragment";

    private BrowseFragment mBrowseFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBrowseFragment = (BrowseFragment) getFragmentManager().findFragmentByTag(
                TAG_BROWSE_FRAGMENT);
        if (mBrowseFragment == null) {
            mBrowseFragment = new BrowseFragment();
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, mBrowseFragment, TAG_BROWSE_FRAGMENT)
                    .commit();
        }

        prepareBackgroundManager();

        ClassPresenterSelector rowPresenterSelector = new ClassPresenterSelector();
        rowPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());

        ObjectAdapter adapter = getBrowseInfoFactory().getRows();
        adapter.setPresenterSelector(rowPresenterSelector);

        mBrowseFragment.setAdapter(adapter);
        updateBrowseParams();

    }

    private void prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(this);
        mBackgroundManager.attach(getWindow());
        mDefaultBackground = getResources().getDrawable(R.drawable.tvbar_bg,null);
        mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
        mBackgroundManager.setDrawable(mDefaultBackground);
    }

    protected void updateBrowseParams() {
        mBrowseFragment.setTitle(getBrowseTitle());
//        mBrowseFragment.setBadgeDrawable(getBadgeImage());
        mBrowseFragment.setHeadersState(BrowseFragment.HEADERS_DISABLED);
    }
}
