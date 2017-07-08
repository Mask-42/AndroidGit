package com.example.manpr.gittest1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by Best Buy on 09-07-2017.
 */
public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.DataObjectHolder> {
    LinkedList<CallDataProvider> DataSet;
    Context con;
    public CallLogAdapter(LinkedList DataSet,Context con) {
        this.DataSet=DataSet;
        this.con=con;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_call_log,parent,false);
        CallLogAdapter.DataObjectHolder doh1=new CallLogAdapter.DataObjectHolder(v);
        Log.e("Debug", "getCallLog: Working",null );
        return doh1;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {
        holder.setIsRecyclable(false);
        String temp1=DataSet.get(position).getPhNumber();
        holder.phoneNo.setText(temp1);
        String temp2="at "+DataSet.get(position).getCallDayTime();
        holder.CallDetails.setText(temp2);
        holder.setAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in= new Intent(con,AddAppointmentActivity.class);
                in.putExtra("Phone",holder.phoneNo.getText().toString());
                con.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {
        return DataSet.size();
    }


    public static class DataObjectHolder extends RecyclerView.ViewHolder{

        TextView phoneNo,CallDetails;
        Button setAppointment;
        public DataObjectHolder(View itemView) {
            super(itemView);
            phoneNo=(TextView)itemView.findViewById(R.id.PhoneNo);
            CallDetails=(TextView)itemView.findViewById(R.id.CallDetails);
            setAppointment=(Button)itemView.findViewById(R.id.SetAppointment);
        }
    }
}