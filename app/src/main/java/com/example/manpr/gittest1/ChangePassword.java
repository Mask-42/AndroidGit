package com.example.manpr.gittest1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ChangePassword extends AppCompatActivity {
    EditText et1,et2,et3;
    Button bt1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        et1=(EditText)findViewById(R.id.CurPass);
        et2=(EditText)findViewById(R.id.NewPass);
        et3=(EditText)findViewById(R.id.ConfirmPass);
        bt1=(Button)findViewById(R.id.PasswordChange);
    }
}
