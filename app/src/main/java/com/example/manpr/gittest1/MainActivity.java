package com.example.manpr.gittest1;

import android.content.Intent;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
EditText et1,et2;
    Button log;
    FirebaseDatabase fbDb=FirebaseDatabase.getInstance();
    DatabaseReference rootRef,UsersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 rootRef = fbDb.getReference();
        UsersRef=rootRef.child("Users");
        Toast.makeText(MainActivity.this, UsersRef.toString(), Toast.LENGTH_LONG).show();

        log=(Button)findViewById(R.id.Login);
        Typeface tf=Typeface.createFromAsset(getAssets(),"font/os.ttf");
        log.setTypeface(tf);
        et1=(EditText)findViewById(R.id.User);
        et2=(EditText)findViewById(R.id.Pass);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserName=et1.getText().toString();
                final String Password=et2.getText().toString();

             UsersRef.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
                     Log.e("MY Log",dataSnapshot.getValue()+"");
                 }

                 @Override
                 public void onCancelled(DatabaseError databaseError) {

                 }
             });

            }
        });
    }
}
