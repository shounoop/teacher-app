package com.example.stdmanager.Settings;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.stdmanager.R;

public class SettingsActivity extends TabActivity {
    TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setControl();
        setEvent();
    }

    /**
     * find view by id for each component
     * <p>
     * instantiate tab host
     */
    private void setControl() {
        tabHost = (TabHost) getTabHost();
    }

    /**
     * find view by id for each component
     * <p>
     * Step 1: instantiate the host
     * Step 2: create tabSpec account
     * Step 3: create tabSpec application
     */
    private void setEvent() {
        tabHost.setup();

        TabHost.TabSpec specAccount = tabHost.newTabSpec("Tài khoản");
        Intent accountIntent = new Intent(SettingsActivity.this, SettingsAccountActivity.class);
        specAccount.setIndicator("Tài khoản");
        specAccount.setContent(accountIntent);
        tabHost.addTab(specAccount);

        TabHost.TabSpec spec = tabHost.newTabSpec("Ứng dụng");
        Intent applicationIntent = new Intent(SettingsActivity.this, SettingsApplicationActivity.class);
        spec.setIndicator("Ứng dụng");
        spec.setContent(applicationIntent);
        tabHost.addTab(spec);
    }
}