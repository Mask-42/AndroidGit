package com.example.manpr.gittest1;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;



/**
 * Created by manpr on 10-07-2017.
 */

public class SettingsPrefScr extends PreferenceActivity implements Preference.OnPreferenceClickListener {
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
       Preference cp=(Preference) findPreference("pass_change");
    cp.setOnPreferenceClickListener(this);
        Preference help=(Preference) findPreference("help");
        help.setOnPreferenceClickListener(this);
        Preference close=(Preference) findPreference("close_set");
        close.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
switch (preference.getKey())
{
    case "pass_change":
        Intent in1=new Intent(SettingsPrefScr.this,ChangePassword.class);
        startActivity(in1);
        break;
    case "help":
        Intent in2=new Intent(SettingsPrefScr.this,HelpActivity.class);
        startActivity(in2);
        break;
    case "close_set":
        finish();
        break;

}

        return false;
    }
}
