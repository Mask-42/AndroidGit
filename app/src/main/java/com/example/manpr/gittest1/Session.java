package com.example.manpr.gittest1;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Best Buy on 12-07-2017.
 */

public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public Session(Context ctx){
        this.ctx=ctx;
        prefs=ctx.getSharedPreferences("MyApp",Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedIn(boolean loggedIn){
        editor.putBoolean("LoggedIn",loggedIn);
        editor.commit();
    }
    public void setUser(String User){
        editor.putString("User",User);
        editor.commit();
    }
    public String getUser(){
        return prefs.getString("User",null);
    }
    public void setTime(String Time){
        editor.putString("MyTime",Time);
        editor.commit();
    }
    public String getTime(){
        return prefs.getString("MyTime","");
    }

    public boolean loggedIn(){
        return prefs.getBoolean("LoggedIn",false);
    }
}
