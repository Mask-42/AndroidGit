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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar tb;
    LinkedList results;  //This is the Data which is being sent to the CardAdapter Class
    RecyclerView rv1;  //This is the instantiation of RecyclerView
    //**The REYCLERVIEW needs two things,   Adapter and  LayoutManager

    RecyclerView.Adapter my_adapter;
    RecyclerView.LayoutManager my_LM;
    Button b1;
    int j = 6;
    SwipeRefreshLayout srl;
    private Session session;
    private TextView navUser;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigationdemo);   //recycler_layout is my Main Layout
        session = new Session(this);
        if (!session.loggedIn()) {
            logout();
        }
        rv1 = (RecyclerView) findViewById(R.id.my_recycler);

        rv1.setHasFixedSize(true);
        refresh();
        tb = (Toolbar) findViewById(R.id.toolbar);
        my_LM = new LinearLayoutManager(this);  //The Linear Layout Manager is added to the Recycler View
        rv1.setLayoutManager(my_LM);
        Bundle b = getIntent().getExtras();
        String from = b.getString("From");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, tb, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        navUser = (TextView) headerView.findViewById(R.id.Nav_User);
        navUser.setText(session.getUser());

        switch (from) {

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

                Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");
                if (managedCursor.getCount() == 0) {
                    Toast.makeText(this, "No Logs", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    managedCursor.moveToFirst();
                    my_adapter = new CallLogAdapter(getCallLog(managedCursor), Main2Activity.this);
                    rv1.setAdapter(my_adapter);
                    //    managedCursor.close();
                }
                break;
            case "Meetings":
                tb.setTitle("Pending Meetings");
                FirebaseDatabase fbDb = FirebaseDatabase.getInstance();
                DatabaseReference RootRef = fbDb.getReference();
                DatabaseReference PendRef = RootRef.child("PendingAppointments");

                Query query = PendRef.orderByChild("ManagerName").equalTo(session.getUser());


                final ArrayList<HashMap<String, String>> arrArrayList = new ArrayList<HashMap<String, String>>();
                query.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final HashMap<String, String> hashMap = new HashMap<String, String>();
                        for (DataSnapshot ds1 : dataSnapshot.getChildren()) {
                            if (ds1.getKey().equals("Name") || ds1.getKey().equals("Contact") || ds1.getKey().equals("Date") || ds1.getKey().equals("Time")) {
                                hashMap.put(ds1.getKey(), (String) ds1.getValue());
                            }
                        }
                        arrArrayList.add(hashMap);
                        rv1.setAdapter(new PendMeetAdapter(arrArrayList, Main2Activity.this));
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private LinkedList<CallDataProvider> getCallLog(Cursor mCursor) {
        int i = 0;
        results = new LinkedList<CallDataProvider>();
        int type = mCursor.getColumnIndex(CallLog.Calls.TYPE);
        String callType = mCursor.getString(type);
        do {
            callType = mCursor.getString(type);
            if (Integer.parseInt(callType) == CallLog.Calls.INCOMING_TYPE) {
                i++;
                CallDataProvider obj = new CallDataProvider(mCursor);
                results.add(obj);
                if (i == 19) {
                    break;
                }
            }
        } while (mCursor.moveToNext());
        return results;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Home) {
            Intent in=new Intent(Main2Activity.this,NavigationDemo.class);
            startActivity(in);
            finish();
        } else if (id == R.id.CallLogs) {
            Intent in1 = new Intent(Main2Activity.this, Main2Activity.class);
            in1.putExtra("From", "Call Log");
            startActivity(in1);
            finish();
        } else if (id == R.id.Meetings) {
            Intent in1 = new Intent(Main2Activity.this, Main2Activity.class);
            in1.putExtra("From", "Meetings");
            startActivity(in1);
            finish();
        } else if (id == R.id.Settings) {
            Intent in2 = new Intent(Main2Activity.this, SettingsPrefScr.class);
            startActivity(in2);
        } else if (id == R.id.Logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        session.setLoggedIn(false);
        finish();
        startActivity(new Intent(Main2Activity.this, MainActivity.class));
    }

    private void refresh() {
        //*************WORKING WITH SWIPE REFRESH LAYOUT**************
        srl = (SwipeRefreshLayout) findViewById(R.id.swipe1);
        srl.setColorSchemeColors(Color.GREEN, Color.BLUE, Color.RED);  //These are the colors of the Rotating Circle
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {  //Listener to tell What to do on Refresh
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);  //This brings the circle down and begins refreshing
                (new Handler()).postDelayed(new Runnable() { //This is the thread handler which keeps the circle there for some time and updates the RecyclerView
                    @Override
                    public void run() {
                        srl.setRefreshing(false);
                        Intent in = new Intent(Main2Activity.this, Main2Activity.class);
                        finish();
                        in.putExtra("From", "Meetings");
                        startActivity(in);
                    }
                }, 2000); //This is the time for which the Refresh will take place (in millis)
            }
        });

    }
}