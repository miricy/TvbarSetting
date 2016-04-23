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

package com.yekertech.tvbarsetting.name;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.provider.Settings;

public class DeviceManager {

    private static final String DEVICE_NAME="device_name";

    /**
     * Retrieves the name from Settings.Global.DEVICE_NAME
     *
     * @param context A context that can access Settings.Global
     * @return The device name.
     */
    public static String getDeviceName(Context context) {
        return Settings.Global.getString(context.getContentResolver(), DEVICE_NAME);
    }

    /**
     * Sets the system device name.
     *
     * For now it will explicitly call the different discoverable services that haven't been ported
     * to use the Settings.Global.DEVICE_NAME entry.
     *
     * @param context A context that can access Settings.Global
     * @param name The new device name.
     */
    public static void setDeviceName(Context context, String name) {
        Settings.Global.putString(context.getContentResolver(), DEVICE_NAME, name);
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            adapter.setName(name);
        }
    }
}
