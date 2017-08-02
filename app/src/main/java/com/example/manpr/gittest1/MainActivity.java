package com.example.manpr.gittest1;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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


    private EditText et1,et2;
    private Button log;
    private FirebaseDatabase fbDb=FirebaseDatabase.getInstance();
    private DatabaseReference rootRef,UsersRef;
    private String UserName=null;
    private String Password=null;
    private Session session;

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
        session=new Session(this);
        if(session.loggedIn()){
            String gR=session.getRole();
            if(gR.equals("Manager"))
            {
            startActivity(new Intent(MainActivity.this,NavigationDemo.class));
            }
            else if(gR.equals("Security"))
            {
                startActivity(new Intent(MainActivity.this,QRScanner.class));
            }

            finish();
        }

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 UserName=et1.getText().toString();
                 Password=et2.getText().toString();
                if(!(TextUtils.isEmpty(UserName)&&TextUtils.isEmpty(Password))) {
                    UsersRef.orderByChild("Username").equalTo(UserName).addListenerForSingleValueEvent(MyListener);
                }
                else{
                    Toast.makeText(MainActivity.this, "Enter Both Username and Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
ValueEventListener MyListener= new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        String Pass=null,Role=null,Use=null;
        for(DataSnapshot ds1: dataSnapshot.getChildren()) {
            for (DataSnapshot ds : ds1.getChildren()) {
                if(ds.getKey().equals("Name")){
                    Use=(String)ds.getValue();
                }
               else if (ds.getKey().equals("Password")) {
                    Pass = (String) ds.getValue();
                } else if (ds.getKey().equals("Role")) {
                    Role = (String) ds.getValue();
                }
            }
        }
        if (Pass.equals(Password)&&Role.equals("Manager")){
            session.setLoggedIn(true);
            session.setUser(Use);
            session.setRole(Role);
            Intent in=new Intent(MainActivity.this,NavigationDemo.class);
            startActivity(in);
            finish();
        }
        else if (Pass.equals(Password)&&Role.equals("Security")){
            session.setLoggedIn(true);
            session.setUser(Use);
            session.setRole(Role);
            Intent in2=new Intent(MainActivity.this,QRScanner.class);
            startActivity(in2);
            finish();
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
