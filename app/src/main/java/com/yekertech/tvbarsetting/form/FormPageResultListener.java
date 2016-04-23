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

package com.yekertech.tvbarsetting.form;

import android.os.Bundle;

/**
 * Listens for a FormPage to be completed.
 */
public interface FormPageResultListener {

    /**
     * Called when the specified page has been completed.
     *
     * @param page the page that has been completed.
     * @param bundleResults the results of the page completion. This bundle
     *            should contain a minimum of a String with the key
     *            {@link FormPage#DATA_KEY_SUMMARY_STRING}. Any other keys are
     *            optional and application-specific.
     */
    void onBundlePageResult(FormPage page, Bundle bundleResults);
}