package com.example.manpr.gittest1;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;

public class HelpActivity extends Activity implements DialogInterface.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("HELP");
        String data="All copyright, trade marks, design rights, patents and other intellectual property rights (registered and unregistered) in and on SAM Online Services and SAM Content belong to the SAM and/or third parties (which may include you or other users.) The SAM reserves all of its rights in SAM Content and SAM Online Services. Nothing in the Terms grants you a right or license to use any trade mark, design right or copyright owned or controlled by the SAM or any other third party except as expressly provided in the Terms.";
        alertDialog.setMessage(data+" ");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK",HelpActivity.this);
        alertDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        finish();
    }
}
