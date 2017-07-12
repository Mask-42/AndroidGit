package com.example.manpr.gittest1;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class NavigationDemo extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView rv1;  //This is the instantiation of RecyclerView
    //**The REYCLERVIEW needs two things,   Adapter and  LayoutManager

    RecyclerView.Adapter my_adapter;
    RecyclerView.LayoutManager my_LM;
    Button b1;
    int j = 6;
    SwipeRefreshLayout srl;
    private Session session;
    private TextView navUser;
    LinkedList<HashMap<String, String>> arrLinkedList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigationdemo);

        session=new Session(this);
        if(!session.loggedIn()){
            logout();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        navUser=(TextView)headerView.findViewById(R.id.Nav_User);
        navUser.setText(session.getUser());

        //*****************************OURS*****************//
        rv1 = (RecyclerView) findViewById(R.id.my_recycler);

        rv1.setHasFixedSize(true);

        my_LM = new LinearLayoutManager(this);  //The Linear Layout Manager is added to the Recycler View
        rv1.setLayoutManager(my_LM);

            setTitle("Confirmed Appointments");
            refresh();
            slide_drag();
        FirebaseDatabase fbDb = FirebaseDatabase.getInstance();
        DatabaseReference RootRef = fbDb.getReference();
        DatabaseReference PendRef = RootRef.child("ConfirmedAppointments");

        Query query = PendRef.orderByChild("ManagerName").equalTo(session.getUser());


        arrLinkedList = new LinkedList<HashMap<String, String>>();
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final HashMap<String, String> hashMap = new HashMap<String, String>();
                for (DataSnapshot ds1 : dataSnapshot.getChildren()) {
                    if (ds1.getKey().equals("Name") || ds1.getKey().equals("Date") || ds1.getKey().equals("Time")||ds1.getKey().equals("Purpose")) {
                        hashMap.put(ds1.getKey(), (String) ds1.getValue());
                    }
                }
                arrLinkedList.addFirst(hashMap);
                rv1.setAdapter(new CardAdapter(arrLinkedList, NavigationDemo.this));
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
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Home) {

        } else if (id == R.id.CallLogs) {
            Intent in1=new Intent(NavigationDemo.this,Main2Activity.class);
            in1.putExtra("From","Call Log");
            startActivity(in1);
        } else if (id == R.id.Meetings) {
            Intent in1=new Intent(NavigationDemo.this,Main2Activity.class);
            in1.putExtra("From","Meetings");
            startActivity(in1);
        } else if (id == R.id.Settings) {
            Intent in2=new Intent(NavigationDemo.this,SettingsPrefScr.class);
            startActivity(in2);
        } else if (id == R.id.Logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void refresh(){
        //*************WORKING WITH SWIPE REFRESH LAYOUT**************
        srl=(SwipeRefreshLayout)findViewById(R.id.swipe1);
        srl.setColorSchemeColors(Color.GREEN,Color.BLUE,Color.RED);  //These are the colors of the Rotating Circle
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {  //Listener to tell What to do on Refresh
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);  //This brings the circle down and begins refreshing
                (new Handler()).postDelayed(new Runnable() { //This is the thread handler which keeps the circle there for some time and updates the RecyclerView
                    @Override
                    public void run() {
                        srl.setRefreshing(false);  //No Further Refreshing allowed meaning you can not Refresh it while it is Refreshing
                        Intent in=new Intent(NavigationDemo.this,NavigationDemo.class);
                        finish();
                        startActivity(in);
                    }
                },2000); //This is the time for which the Refresh will take place (in millis)
            }
        });

    }
    private void slide_drag(){
        //ItemTouchHelper is the Class which helps us in adding functionality to the Swipe and the drag gestures
        ItemTouchHelper ith= new ItemTouchHelper(createHelperCallback());  //createHelperCallback is defined below after some time
        ith.attachToRecyclerView(rv1);

    }

    //************The createHelperCallback Method*************
    private ItemTouchHelper.Callback createHelperCallback() {
        final float ALPHA_FULL=1.0f;
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = //We Make an object of SimpleCallback which enables us to add the gestures

                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,   //The first two i.e. up or down are for the move operation
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {  //Left and right are for swipes....keep in mind only two parametres are there

                    //********The onMove method tells what do to when an Item is dragged********
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition()); //Defined later
                        return true;
                    }

                    //*********The onSwiped method tells what to do when an Item is swiped **********
                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    }

                    //********This is a method used to disable swipe refresh layout when we are dragging an item down*******
                    @Override
                    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                        super.onSelectedChanged(viewHolder, actionState);
                        final boolean swiping = actionState == ItemTouchHelper.ACTION_STATE_DRAG;
                        srl.setEnabled(!swiping);
                    }

                    //*******This is the Method buy which the fade-out animation is put on the Swipe of Card********
                    @Override
                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        if(actionState==ItemTouchHelper.ACTION_STATE_SWIPE){
                            final float alpha = ALPHA_FULL-Math.abs(dX)/(float)viewHolder.itemView.getWidth();
                            viewHolder.itemView.setAlpha(alpha);
                            viewHolder.itemView.setTranslationX(dX);
                        }
                        else{
                            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        }
                    }
                };
        return simpleItemTouchCallback;
    }
    //************To Move the item in the result from one index to another used in onMove method of ItemTouchHelper*******
    private void moveItem(int oldPos, int newPos) {

        HashMap<String,String> item=arrLinkedList.get(oldPos);
        arrLinkedList.remove(oldPos);
        arrLinkedList.add(newPos, item);
        my_adapter.notifyItemMoved(oldPos, newPos);
    }

    private void logout(){
        session.setLoggedIn(false);
        finish();
        startActivity(new Intent(NavigationDemo.this,MainActivity.class));
    }
}
