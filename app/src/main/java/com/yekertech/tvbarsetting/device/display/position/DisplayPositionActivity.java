package com.yekertech.tvbarsetting.device.display.position;

import com.yekertech.tvbarsetting.R;
import com.yekertech.tvbarsetting.dialog.old.Action;
import com.yekertech.tvbarsetting.dialog.old.ActionAdapter;
import com.yekertech.tvbarsetting.dialog.old.ActionFragment;
import com.yekertech.tvbarsetting.dialog.old.ContentFragment;
import com.yekertech.tvbarsetting.dialog.old.DialogActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.droidlogic.app.DisplayPositionManager;


public class DisplayPositionActivity extends DialogActivity implements ActionAdapter.Listener {
    private static final String zoom_in = "zoom in";
    private static final String zoom_out = "zoom out";

    private ContentFragment mContentFragment;
    private ActionFragment mActionFragment;
    private DisplayPositionManager mDisplayPositionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDisplayPositionManager = new DisplayPositionManager(this);
        mContentFragment = createMainMenuContentFragment();
        mActionFragment = ActionFragment.newInstance(getMainActions());
        setContentAndActionFragments(mContentFragment, mActionFragment);
    }

    @Override
    protected void onPause() {
        mDisplayPositionManager.saveDisplayPosition();
        super.onPause();
    }

    @Override
    public void onActionClicked(Action action) {
        String mode = action.getKey();

        if (mode.equals(zoom_in)) {
            mDisplayPositionManager.zoomIn();
        } else {
            mDisplayPositionManager.zoomOut();
        }
        updateMainScreen();
    }

    private void goToMainScreen() {
        updateMainScreen();
        getFragmentManager().popBackStack(null, 0);
    }

    private void updateMainScreen() {
        mContentFragment.setDescriptionText(
            getString(R.string.device_position_description) + " " + mDisplayPositionManager.getCurrentRateValue()+ "%");
    }

    private ContentFragment createMainMenuContentFragment() {
        return ContentFragment.newInstance(
                getString(R.string.device_position), getString(R.string.device_display),
                getString(R.string.device_position_description) + " " + mDisplayPositionManager.getInitialRateValue()+ "%",
                R.drawable.ic_settings_overscan,
                getResources().getColor(R.color.icon_background));
    }

    private ArrayList<Action> getMainActions() {
        ArrayList<Action> actions = new ArrayList<Action>();

        actions.add(new Action.Builder().key(zoom_in)
            .title("               "+getString(R.string.device_position_zoomin))
            .build());

        actions.add(new Action.Builder().key(zoom_out)
            .title("               "+getString(R.string.device_position_zoomout))
            .build());

        return actions;
    }
}
