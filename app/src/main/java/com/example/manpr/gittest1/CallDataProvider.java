package com.example.manpr.gittest1;

import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import java.util.Date;

/**
 * Created by Best Buy on 09-07-2017.
 */

public class CallDataProvider {
    private Cursor managedCursor;
    private int number,date;
    private String phNumber,callDate;
    private Date callDayTime;
    public CallDataProvider(Cursor managedCursor){
        this.managedCursor=managedCursor;
         number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);

         date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
         phNumber = managedCursor.getString(number);

         callDate = managedCursor.getString(date);
         callDayTime = new Date(Long.valueOf(callDate));
        Log.e("Debug", "getCallLog: Working",null );

    }

    public String getPhNumber() {
        return phNumber;
    }

    public String getCallDayTime() {
        String temp=String.valueOf(callDayTime);
        String temp1=temp.substring(0,19);
        return temp1;
    }
}
