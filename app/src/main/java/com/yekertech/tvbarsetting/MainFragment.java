/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.yekertech.tvbarsetting;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yekertech.tvbarsetting.about.AboutActivity;
import com.yekertech.tvbarsetting.connectivity.NetworkActivity;
import com.yekertech.tvbarsetting.device.display.DisplayActivity;
import com.yekertech.tvbarsetting.device.sound.SoundActivity;

public class MainFragment extends BrowseFragment {
    private static final String TAG = "MainFragment";

    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int GRID_ITEM_WIDTH = 200;
    private static final int GRID_ITEM_HEIGHT = 200;
    private static final int NUM_ROWS = 1;
    private static final int NUM_COLS = 6;

    private final Handler mHandler = new Handler();
    private ArrayObjectAdapter mRowsAdapter;
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private URI mBackgroundURI;
    private BackgroundManager mBackgroundManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);

        prepareBackgroundManager();

        setupUIElements();

//        loadRows();
        loadMenuItem();
        setupEventListeners();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mBackgroundTimer) {
            Log.d(TAG, "onDestroy: " + mBackgroundTimer.toString());
            mBackgroundTimer.cancel();
        }
    }

    private void loadMenuItem()
    {
        int i=0;
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new MenuItemPresenter());

        Intent intentDisp=new Intent(Intent.ACTION_MAIN);
        intentDisp.setClass(this.getActivity(), DisplayActivity.class);

        Intent intentBt = new Intent(Intent.ACTION_MAIN);
        intentBt.setComponent(new ComponentName("com.android.settings","com.android.settings.SubSettings"));
        intentBt.putExtra(":settings:show_fragment", "com.android.settings.bluetooth.BluetoothSettings");
        intentBt.putExtra(":settings:show_fragment_title", "Bluetooth");

        Intent intentNetwork=new Intent(Intent.ACTION_MAIN);
        intentNetwork.setClass(this.getActivity(), NetworkActivity.class);

        Intent intentSound=new Intent(Intent.ACTION_MAIN);
        intentSound.setClass(this.getActivity(), SoundActivity.class);

        Intent intentCommon=new Intent(Intent.ACTION_MAIN);
        intentCommon.setClass(this.getActivity(), AboutActivity.class);

        Intent intentAbout=new Intent(Intent.ACTION_MAIN);
        intentAbout.setClass(this.getActivity(), AboutActivity.class);


        listRowAdapter.add(new MenuItem.Builder()
                .id(i++)
                .title("图像")
                .backgroundColor(0xff2daae6)
                .imageResourceId(this.getActivity(), R.drawable.ic_settings_display)
                .intent(intentDisp)
                .build());

        listRowAdapter.add(new MenuItem.Builder()
                .id(i++)
                .title("声音")
                .backgroundColor(0xffe6b41e)
                .imageResourceId(this.getActivity(), R.drawable.ic_settings_sound_on)
                .intent(intentSound)
                .build());




        listRowAdapter.add(new MenuItem.Builder()
                .id(i++)
                .title("蓝牙设置")
                .backgroundColor(0xff329696)
                .imageResourceId(this.getActivity(), R.drawable.ic_settings_bluetooth)
                .intent(intentBt)
                .build());

        listRowAdapter.add(new MenuItem.Builder()
                .id(i++)
                .title("网络设置")
                .backgroundColor(0xfff08c23)
                .imageResourceId(this.getActivity(), R.drawable.ic_settings_ethernet)
                .intent(intentNetwork)
                .build());

        listRowAdapter.add(new MenuItem.Builder()
                .id(i++)
                .title("通用设置")
                .backgroundColor(0xff737ddc)
                .imageResourceId(this.getActivity(), R.drawable.ic_settings_app_icon)
                .intent(intentCommon)
                .build());

        listRowAdapter.add(new MenuItem.Builder()
                .id(i++)
                .title("关于")
                .backgroundColor(0xfff55a46)
                .imageResourceId(this.getActivity(), R.drawable.ic_settings_about)
                .intent(intentAbout)
                .build());

        HeaderItem header = new HeaderItem(0, "设置");
        mRowsAdapter.add(new ListRow(listRowAdapter));
        setAdapter(mRowsAdapter);
    }

    private void loadRows() {
        List<Movie> list = MovieList.setupMovies();

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        CardPresenter cardPresenter = new CardPresenter();

        int i;
        for (i = 0; i < NUM_ROWS; i++) {
            if (i != 0) {
                Collections.shuffle(list);
            }
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
            for (int j = 0; j < NUM_COLS; j++) {
                listRowAdapter.add(list.get(j));
            }
            HeaderItem header = new HeaderItem(i, MovieList.MOVIE_CATEGORY[i]);
            mRowsAdapter.add(new ListRow( listRowAdapter));
        }

//        HeaderItem gridHeader = new HeaderItem(i, "PREFERENCES");
//
//        GridItemPresenter mGridPresenter = new GridItemPresenter();
//        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
//        gridRowAdapter.add(getResources().getString(R.string.grid_view));
//        gridRowAdapter.add(getString(R.string.error_fragment));
//        gridRowAdapter.add(getResources().getString(R.string.personal_settings));
//        mRowsAdapter.add(new ListRow(gridHeader, gridRowAdapter));

        setAdapter(mRowsAdapter);

    }

    private void prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mDefaultBackground = getResources().getDrawable(R.drawable.tvbar_bg,null);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
        mBackgroundManager.setDrawable(mDefaultBackground);
    }

    private void setupUIElements() {
        // setBadgeDrawable(getActivity().getResources().getDrawable(
        // R.drawable.videos_by_google_banner));
        setTitle(getResources().getString(R.string.browse_title)); // Badge, when set, takes precedent
        // over title
        setHeadersState(HEADERS_DISABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(getResources().getColor(R.color.fastlane_background));

        // set search icon color
//        setSearchAffordanceColor(getResources().getColor(R.color.search_opaque));
    }

    private void setupEventListeners() {
//        setOnSearchClickedListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), "Implement your own in-app search", Toast.LENGTH_LONG)
//                        .show();
//            }
//        });

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    protected void updateBackground(String uri) {
        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;
        Glide.with(getActivity())
                .load(uri)
                .centerCrop()
                .error(mDefaultBackground)
                .into(new SimpleTarget<GlideDrawable>(width, height) {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable>
                                                        glideAnimation) {
                        mBackgroundManager.setDrawable(resource);
                    }
                });
        mBackgroundTimer.cancel();
    }

    private void startBackgroundTimer() {
        if (null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Movie) {
                Movie movie = (Movie) item;
                Log.d(TAG, "Item: " + item.toString());
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.MOVIE, movie);

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        DetailsActivity.SHARED_ELEMENT_NAME).toBundle();
                getActivity().startActivity(intent, bundle);
            } else if (item instanceof MenuItem) {
                MenuItem menuItem=(MenuItem)item;
                if (menuItem.getIntent() != null) {
                    try {
                        MainFragment.this.getActivity().startActivity(menuItem.getIntent());
                    } catch (ActivityNotFoundException e) {
                        Log.e(TAG, "Activity not found", e);
                    }
                }
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Movie) {
                mBackgroundURI = ((Movie) item).getBackgroundImageURI();
                startBackgroundTimer();
            }

        }
    }

    private class UpdateBackgroundTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mBackgroundURI != null) {
                        updateBackground(mBackgroundURI.toString());
                    }
                }
            });

        }
    }

    private class GridItemPresenter extends Presenter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            LayoutInflater inflater=LayoutInflater.from(parent.getContext());
            View viewDim=inflater.inflate(R.layout.main_item,null);
            TextView view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setBackgroundColor(getResources().getColor(R.color.default_background));
            view.setTextColor(Color.WHITE);
            view.setGravity(Gravity.CENTER);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            ((TextView) viewHolder.view).setText((String) item);
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {
        }
    }

}
