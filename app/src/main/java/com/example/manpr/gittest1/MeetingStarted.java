package com.example.manpr.gittest1;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MeetingStarted extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {
    private long startTime;
    private Session session;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private TextView Mname, Mtime, Mpurpose, TimeStarted;
    private Button Begin, Finish, Cancel;
    private DatabaseReference rootRef, conRef,toPath1;
    private String Mtime2, Mdate2;
    private String Times="",FinishTime="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_started);
        setTitle("Meeting Details");
        session = new Session(MeetingStarted.this);
        session.setTime(Times);
        Mname = (TextView) findViewById(R.id.Name_Visitor);
        Mtime = (TextView) findViewById(R.id.Scheduled_Time);
        Mpurpose = (TextView) findViewById(R.id.Purpose_Visitor);
        TimeStarted = (TextView) findViewById(R.id.Time_Started);

        Begin = (Button) findViewById(R.id.StartMeeting);
        Finish = (Button) findViewById(R.id.StopMeeting);
        Cancel = (Button) findViewById(R.id.CancelMeeting);

        Begin.setOnClickListener(this);
        Finish.setOnClickListener(this);
        Cancel.setOnClickListener(this);


        Bundle b = getIntent().getExtras();
        Mname.append(b.getString("Mname"));
        Mtime.setText(b.getString("Mtime"));
        Mpurpose.append(b.getString("Mpurpose"));
        Mtime2 = b.getString("Mtime2");
        Mdate2 = b.getString("Mdate2");
        rootRef = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.StartMeeting:
                startTime = System.currentTimeMillis();
                Times = dateFormat.format(new Date(startTime));
                TimeStarted.append(Times);
                Finish.setClickable(true);
                Begin.setClickable(false);
                session.setTime(Times);
                break;
            case R.id.StopMeeting:
                final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                FinishTime = dateFormat.format(new Date(System.currentTimeMillis()));

                final DatabaseReference[] fromPath = new DatabaseReference[1];
                final DatabaseReference toPath;
                conRef = rootRef.child("ConfirmedAppointments");
                toPath = rootRef.child("Log/");
                final String[] key = new String[1];
                Query query = conRef.orderByChild("ManagerName_Date_Time").equalTo(session.getUser() + "_" + Mdate2 + "_" + Mtime2);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            key[0] = ds.getKey().toString();
                        }
                         fromPath[0] =conRef.child(key[0]);
                        moveFirebaseRecord(fromPath[0],toPath,1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                alertDialog.setMessage("Meeting Ended at " + FinishTime);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", MeetingStarted.this);
                alertDialog.show();
                break;
            case R.id.CancelMeeting:
                final AlertDialog alertDialog1 = new AlertDialog.Builder(this).create();
                alertDialog1.setMessage("Are You Sure You Want To Cancel This Appointment? ");
                alertDialog1.setButton(DialogInterface.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final DatabaseReference[] fromPath1 = new DatabaseReference[1];
                        conRef = rootRef.child("ConfirmedAppointments");
                        toPath1 = rootRef.child("Log");
                        final String[] key1 = new String[1];
                        Query query1 = conRef.orderByChild("ManagerName_Date_Time").equalTo(session.getUser() + "_" + Mdate2 + "_" + Mtime2);
                        query1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    key1[0] = ds.getKey().toString();
                                }
                                fromPath1[0] =conRef.child(key1[0]);
                                moveFirebaseRecord(fromPath1[0],toPath1,0);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        finish();
                    }
                });
                alertDialog1.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog1.dismiss();
                    }
                });
                alertDialog1.show();
                break;
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        finish();
    }

    public void moveFirebaseRecord(DatabaseReference fromPath, final DatabaseReference toPath, final int flag) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String MyKey=toPath.push().getKey();
                        toPath.child(MyKey).setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        if (databaseError != null) {
                            System.out.println("Copy failed");
                        } else {
                            System.out.println("Success");
                        }
                    }
                });
                if(flag==1){
                toPath.child(MyKey).child("Status").setValue("Success");}
                else{
                    toPath.child(MyKey).child("Status").setValue("Canceled");
                }
                toPath.child(MyKey).child("Meeting Over At").setValue(FinishTime);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Copy failed");
            }
        });
        fromPath.removeValue();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TimeStarted.append(session.getTime());
        if(session.getTime().equals("")){
            Begin.setClickable(true);
            Finish.setClickable(false);
        }
        else{
            Begin.setClickable(false);
            Finish.setClickable(true);
        }
    }
}
