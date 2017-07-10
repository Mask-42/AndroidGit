package com.example.manpr.gittest1;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
EditText et1,et2;
    Button log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et1=(EditText)findViewById(R.id.User);
        et2=(EditText)findViewById(R.id.Pass);
        log=(Button)findViewById(R.id.Login);
        Typeface tf=Typeface.createFromAsset(getAssets(),"font/os.ttf");
        log.setTypeface(tf);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MainActivity.this,NavigationDemo.class);
                startActivity(in);
            }
        });
    }
}
