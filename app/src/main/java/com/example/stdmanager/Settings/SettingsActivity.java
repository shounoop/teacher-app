package com.example.stdmanager.Settings;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.stdmanager.R;

public class SettingsActivity extends TabActivity {
    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setControl();
        setEvent();
    }

    private void setControl() {
        this.tabHost = (TabHost) getTabHost();
    }

    private void setEvent() {
        TabHost.TabSpec specAccount = this.tabHost.newTabSpec("Tài khoản");
        Intent accountIntent = new Intent(SettingsActivity.this, SettingsAccountActivity.class);
        specAccount.setIndicator("Tài khoản");
        specAccount.setContent(accountIntent);
        this.tabHost.addTab(specAccount);

        TabHost.TabSpec spec = this.tabHost.newTabSpec("Ứng dụng");
        Intent applicationIntent = new Intent(SettingsActivity.this, SettingsApplicationActivity.class);
        spec.setIndicator("Ứng dụng");
        spec.setContent(applicationIntent);
        this.tabHost.addTab(spec);
    }
}