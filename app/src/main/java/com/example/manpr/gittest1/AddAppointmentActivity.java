package com.example.manpr.gittest1;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import android.text.format.DateFormat;
import android.widget.Toast;

import java.util.Calendar;

public class AddAppointmentActivity extends AppCompatActivity implements View.OnClickListener,DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener, View.OnFocusChangeListener {
    EditText time,date;
    TextView phone_no;
    Button submit;
    String Phone;
    int year,month,day,hours,minutes;
    int yearF,monthF,dayF,hoursF,minutesF;
    ConstraintLayout con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
        Bundle bundle=getIntent().getExtras();
        Phone=bundle.getString("Phone");

        setTitle("Fix Appointment");
        time=(EditText)findViewById(R.id.Time);
        date=(EditText)findViewById(R.id.Date);
        phone_no=(TextView)findViewById(R.id.PhoneText);
        submit=(Button)findViewById(R.id.Submit1);
        con=(ConstraintLayout)findViewById(R.id.Constraint1);
        con.requestFocus();


        phone_no.setText(Phone);
        time.setOnFocusChangeListener(this);
        date.setOnFocusChangeListener(this);
        submit.setOnClickListener(this);

    }

    private void sendSMS(){
        String Message="Your request for the Appointment has been accepted, please visit the following link to complete the process.YOUR URL HERE";
        SmsManager sm1=SmsManager.getDefault();
        PendingIntent pi=PendingIntent.getBroadcast(this,1,new Intent("com.example.manpr.gittest1.smssent"),0);
        sm1.sendTextMessage(Phone,null,Message,pi,null); //phone number, copy number, message context, pending intent, delivery pending intent

        registerReceiver(br1,new IntentFilter("com.example.manpr.gittest1.smssent"));

    }

    BroadcastReceiver br1=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status="";
            boolean error=true;

            switch (getResultCode()){
                case Activity.RESULT_OK:
                    error=false;
                    status="Message Sent";
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    status="Error";
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    status="No Service";
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    status="No Data";
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    status="Airplane Mode On";
                    break;
            }
            Toast toast=Toast.makeText(context, status, Toast.LENGTH_SHORT);
           toast.getView().setBackgroundTintList(ColorStateList.valueOf(error? Color.RED:Color.parseColor("#17996e")));
            TextView text=(TextView) toast.getView().findViewById(android.R.id.message);
            text.setTextColor(Color.WHITE);
            toast.show();
        }
    };
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.PasswordChange:

                String[] perms = new String[]{Manifest.permission.SEND_SMS};
                ActivityCompat.requestPermissions(this, perms, 30);
                sendSMS();
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        yearF=i;
        monthF=i1+1;
        dayF=i2;

        date.setText(String.valueOf(dayF)+"/"+String.valueOf(monthF)+"/"+String.valueOf(yearF));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

        hoursF=i;
        minutesF=i1;
        if(minutesF>9){
            time.setText(String.valueOf(hoursF)+" :"+String.valueOf(minutesF));
            }
         else{
         time.setText(String.valueOf(hoursF)+" :0"+String.valueOf(minutesF));
            }
    }
    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()){
            case R.id.Time:

                if(b){
                    Calendar c1= Calendar.getInstance();
                    hours=c1.get(Calendar.HOUR_OF_DAY);
                    minutes=c1.get(Calendar.MINUTE);

                    TimePickerDialog timePicker= new TimePickerDialog(AddAppointmentActivity.this,AddAppointmentActivity.this,hours,minutes, DateFormat.is24HourFormat(this));
                    timePicker.show();}
                break;
            case R.id.Date:

                if(b){
                    Calendar c=Calendar.getInstance();
                    year=c.get(Calendar.YEAR);
                    month=c.get(Calendar.MONTH);
                    day=c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePicker= new DatePickerDialog(AddAppointmentActivity.this,AddAppointmentActivity.this,year,month,day);

                    datePicker.show();}
                break;

        }
    }
}
