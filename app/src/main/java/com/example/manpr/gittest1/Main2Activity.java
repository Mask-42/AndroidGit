package com.example.manpr.gittest1;

//#13eaa2 is the teal color
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    ArrayList<DataProvider> results;  //This is the Data which is being sent to the CardAdapter Class
    RecyclerView rv1;  //This is the instantiation of RecyclerView
    //**The REYCLERVIEW needs two things,   Adapter and  LayoutManager

    RecyclerView.Adapter my_adapter;
    RecyclerView.LayoutManager my_LM;
    Button b1;
    int j=6;
    SwipeRefreshLayout srl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_layout);   //recycler_layout is my Main Layout

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

        /////**** start*****/////
        rv1=(RecyclerView)findViewById(R.id.my_recycler);

        //ItemTouchHelper is the Class which helps us in adding functionality to the Swipe and the drag gestures
        ItemTouchHelper ith= new ItemTouchHelper(createHelperCallback());  //createHelperCallback is defined below after some time
        ith.attachToRecyclerView(rv1);

        rv1.setHasFixedSize(true);

        my_LM = new LinearLayoutManager(this);  //The Linear Layout Manager is added to the Recycler View
        rv1.setLayoutManager(my_LM);

        my_adapter = new CardAdapter(getDataSet(),Main2Activity.this);  //CardAdapter is user-defined Adapter class a.k.a CustomAdapter
        //getDataSet is a method defined below which returns an ArrayList of Objects of the DataProvider Class
        rv1.setAdapter(my_adapter);

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
        results.add(dataObj);  //We added an Object of the DataProvider Class to the results ArrayList
        my_adapter.notifyItemInserted(results.indexOf(dataObj)); //This is used for notification purpose i.e. tells the adapter that gesture has taken place
    }
    //************The deleteItem used in onSwiped method of ItemTouchHelper********
    public void deleteItem(int index){
        results.remove(index);
        my_adapter.notifyItemRemoved(index);
    }

    //************To Move the item in the result from one index to another used in onMove method of ItemTouchHelper*******
    private void moveItem(int oldPos, int newPos) {

        DataProvider item=results.get(oldPos);
        results.remove(oldPos);
        results.add(newPos, item);
        my_adapter.notifyItemMoved(oldPos, newPos);
    }


    //*********This is the getDataSet method used in the CardAdapter constructor***********
    private ArrayList<DataProvider> getDataSet(){
        results= new ArrayList<DataProvider>();
        for(int i=0;i<6;i++){
            DataProvider obj=new DataProvider("Name of visitor "+(i+1),"Time "+(i+1),"Purpose: "+(i+1),"Purpose of the meeting "+(i+1));
            results.add(i,obj);
        }
        return results;  //This returns the ArrayList of the objects of DataProvider Class*********
    }
}
