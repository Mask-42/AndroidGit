package com.example.manpr.gittest1;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
EditText t1;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        Typeface face= Typeface.createFromAsset(getAssets(),"font/Oswald-Light.ttf");

        b1=(Button) findViewById(R.id.button);
        b1.setTypeface(face);*/
    }
}
