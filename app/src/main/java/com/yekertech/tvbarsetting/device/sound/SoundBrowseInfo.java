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

package com.yekertech.tvbarsetting.device.sound;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;

import com.yekertech.tvbarsetting.BrowseInfoBase;
import com.yekertech.tvbarsetting.MenuItem;
import com.yekertech.tvbarsetting.R;
import com.yekertech.tvbarsetting.SettingsConstant;

/**
 * Gets the list of browse headers and browse items.
 */
public class SoundBrowseInfo extends BrowseInfoBase {

    private static final boolean WIFI_DISPLAY_SUPPORTED = false;
    private static final int HEADER_ID = 1;
    private final Context mContext;
    private final ArrayObjectAdapter mRow = new ArrayObjectAdapter();
    private int mNextItemId;

    SoundBrowseInfo(Context context) {
        mContext = context;
        mNextItemId = 0;
        mHeaderItems.add(new HeaderItem(HEADER_ID, mContext.getString(R.string.device_sound)));
        mRows.put(HEADER_ID, mRow);
    }

    public void refreshContent() {
        init();
    }

    void init() {
        mRow.clear();
        mRow.add(new MenuItem.Builder().id(mNextItemId++)
                .title(mContext.getString(R.string.device_sound_effects))
                .imageResourceId(mContext, R.drawable.ic_settings_sound_on)
                .intent(getIntent(SettingsConstant.PACKAGE,
                        SettingsConstant.PACKAGE + ".device.sound.systemsound.SystemSoundActivity"))
                .build());

        if (SettingsConstant.needDroidlogicMboxFeature(mContext)) {
            mRow.add(new MenuItem.Builder().id(mNextItemId++)
                    .title(mContext.getString(R.string.device_sound_digital))
                    .imageResourceId(mContext, R.drawable.ic_settings_sound_on)
                    .intent(getIntent(SettingsConstant.PACKAGE,
                            SettingsConstant.PACKAGE + ".device.sound.digitalsound.DigitalSoundActivity"))
                    .build());
        }
    }

    private Intent getIntent(String targetPackage, String targetClass) {
        ComponentName componentName = new ComponentName(targetPackage, targetClass);
        Intent i = new Intent();
        i.setComponent(componentName);
        return i;
    }
}
