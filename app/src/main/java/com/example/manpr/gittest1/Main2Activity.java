package com.example.manpr.gittest1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.LinkedList;

public class Main2Activity extends AppCompatActivity {

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
        setContentView(R.layout.recycler_layout);   //recycler_layout is my Main Layout

        rv1 = (RecyclerView) findViewById(R.id.my_recycler);

        rv1.setHasFixedSize(true);

        my_LM = new LinearLayoutManager(this);  //The Linear Layout Manager is added to the Recycler View
        rv1.setLayoutManager(my_LM);

        {
            setTitle("Recent Received Calls");
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
        }

        /*{
            setTitle("Recent Apointments");
            refresh();
            slide_drag();
            my_adapter = new CardAdapter(getDataSet(), Main2Activity.this);  //CardAdapter is user-defined Adapter class a.k.a CustomAdapter
            //getDataSet is a method defined below which returns an LinkedList of Objects of the DataProvider Class
            rv1.setAdapter(my_adapter);
        }*/

        //This Button is simply there to add a new card whenever clicked
        b1=(Button)findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataProvider temp= new DataProvider("Newly Added Main "+(j+1),"Newly Added Comment "+(j+1),"Hidden "+(j+1),"Second Hidden"+(j+1));
                j++;
                addItem(temp); //This is a method defined below
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                        for(int k=0;k<3;k++)
                            b1.performClick();  //Just for Checking the Working I called the Button click which was adding an item in the View
                    }
                },3000); //This is the time for which the Refresh will take place (in millis)
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
                        deleteItem(viewHolder.getAdapterPosition()); //Defined Later
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


    //********The addItem method used by the Button**********
    public void addItem(DataProvider dataObj){
        results.addFirst(dataObj);
        //We added an Object of the DataProvider Class to the results LinkedList
        my_adapter.notifyItemInserted(results.indexOf(dataObj)); //This is used for notification purpose i.e. tells the adapter that gesture has taken place
    }
    //************The deleteItem used in onSwiped method of ItemTouchHelper********
    public void deleteItem(int index){
        results.remove(index);
        my_adapter.notifyItemRemoved(index);
    }

    //************To Move the item in the result from one index to another used in onMove method of ItemTouchHelper*******
    private void moveItem(int oldPos, int newPos) {

        DataProvider item=(DataProvider)results.get(oldPos);
        results.remove(oldPos);
        results.add(newPos, item);
        my_adapter.notifyItemMoved(oldPos, newPos);
    }


    //*********This is the getDataSet method used in the CardAdapter constructor***********
    private LinkedList<DataProvider> getDataSet(){
        results= new LinkedList<DataProvider>();
        for(int i=0;i<6;i++){
            DataProvider obj=new DataProvider("The Main Text "+(i+1),"The Comment Text "+(i+1),"Hidden "+(i+1),"Second Hidden"+(i+1));
            results.add(i,obj);
        }
        return results;  //This returns the LinkedList of the objects of DataProvider Class*********
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
}