package com.example.manpr.gittest1;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
EditText et1,et2;
    Button log;
    FirebaseDatabase fbDb=FirebaseDatabase.getInstance();
    DatabaseReference rootRef,UsersRef;
    String UserName=null;
    String Password=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 rootRef = fbDb.getReference();
        UsersRef=rootRef.child("Users");
        log=(Button)findViewById(R.id.Login);
        Typeface tf=Typeface.createFromAsset(getAssets(),"font/os.ttf");
        log.setTypeface(tf);
        et1=(EditText)findViewById(R.id.User);
        et2=(EditText)findViewById(R.id.Pass);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 UserName=et1.getText().toString();
                 Password=et2.getText().toString();
             UsersRef.orderByChild("Username").equalTo(UserName).addListenerForSingleValueEvent(MyListener);
            }
        });
    }
ValueEventListener MyListener= new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        String Pass=null,Role=null;
        for(DataSnapshot ds1: dataSnapshot.getChildren()) {
            for (DataSnapshot ds : ds1.getChildren()) {
                if (ds.getKey().equals("Password")) {
                    Pass = (String) ds.getValue();
                } else if (ds.getKey().equals("Role")) {
                    Role = (String) ds.getValue();
                }
            }
        }
        if (Pass.equals(Password)&&Role.equals("Manager")){
            Intent in=new Intent(MainActivity.this,NavigationDemo.class);
            in.putExtra("User",UserName);
            startActivity(in);
        }
        else {
           Toast toast= Toast.makeText(MainActivity.this, "Wrong Username or Password", Toast.LENGTH_SHORT);
            toast.getView().setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            TextView text=(TextView) toast.getView().findViewById(android.R.id.message);
            text.setTextColor(Color.WHITE);
            toast.show();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
Log.e("TAG",databaseError.getMessage());
    }
};
}
