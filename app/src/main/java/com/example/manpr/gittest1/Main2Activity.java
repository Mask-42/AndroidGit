package com.example.manpr.gittest1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.LinkedList;

public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    Toolbar tb;
    LinkedList results;  //This is the Data which is being sent to the CardAdapter Class
    RecyclerView rv1;  //This is the instantiation of RecyclerView
    //**The REYCLERVIEW needs two things,   Adapter and  LayoutManager

    RecyclerView.Adapter my_adapter;
    RecyclerView.LayoutManager my_LM;
    Button b1;
    int j = 6;
    SwipeRefreshLayout srl;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigationdemo);   //recycler_layout is my Main Layout

        rv1 = (RecyclerView) findViewById(R.id.my_recycler);

        rv1.setHasFixedSize(true);
tb=(Toolbar)findViewById(R.id.toolbar);
        my_LM = new LinearLayoutManager(this);  //The Linear Layout Manager is added to the Recycler View
        rv1.setLayoutManager(my_LM);
        Bundle b=getIntent().getExtras();
        String from=b.getString("From") ;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, tb, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        switch (from){

            case "Call Log":
            tb.setTitle("Recent Received Calls");
            srl = (SwipeRefreshLayout) findViewById(R.id.swipe1);
            srl.setEnabled(false);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                String[] perms = new String[]{Manifest.permission.READ_CALL_LOG};
                ActivityCompat.requestPermissions(this, perms, 10);
                return;
            }

            Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null,null);
            if(managedCursor.getCount()==0){
                Toast.makeText(this, "No Logs", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
             managedCursor.moveToFirst();
                my_adapter = new CallLogAdapter(getCallLog(managedCursor), Main2Activity.this);
                rv1.setAdapter(my_adapter);
                //    managedCursor.close();
            }
            break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
        private LinkedList<CallDataProvider> getCallLog(Cursor mCursor) {
        int i=0;
        results= new LinkedList<CallDataProvider>();
        int type = mCursor.getColumnIndex(CallLog.Calls.TYPE);
        String callType = mCursor.getString(type);
        do{
            callType = mCursor.getString(type);
            if(Integer.parseInt(callType)==CallLog.Calls.INCOMING_TYPE) {
                i++;
                CallDataProvider obj = new CallDataProvider(mCursor);
                results.add(obj);
                if(i==19){
                    break;
                }
            }
        }while(mCursor.moveToNext());
        return results;
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Home) {
            finish();

        } else if (id == R.id.CallLogs) {

        } else if (id == R.id.Meetings) {

        } else if (id == R.id.Settings) {
            Intent in2=new Intent(Main2Activity.this,SettingsPrefScr.class);
            startActivity(in2);
        } else if (id == R.id.Logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}