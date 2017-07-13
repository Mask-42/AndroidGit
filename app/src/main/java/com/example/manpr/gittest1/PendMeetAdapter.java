package com.example.manpr.gittest1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Best Buy on 12-07-2017.
 */

public class PendMeetAdapter extends RecyclerView.Adapter<PendMeetAdapter.ViewHolder> {
   private ArrayList<HashMap<String,String>> arr;
    Context con;
    public PendMeetAdapter(ArrayList<HashMap<String,String>> arr,Context con) {
        this.con=con;
    this.arr=arr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pendmeet_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String name="Name: "+arr.get(position).get("Name");
        String contact="Contact: "+arr.get(position).get("Contact");
        String date="Date: "+arr.get(position).get("Date");
        String time="Time: "+arr.get(position).get("Time");
        holder.V_name.setText(name);
        holder.V_Contact.setText(contact);
        holder.DateOfApp.setText(date);
        holder.TimeofApp.setText(time);
        if(arr.get(position).get("Name")!=null){
            holder.V_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in= new Intent(con,AddAppointmentActivity.class);
                    in.putExtra("Phone",holder.V_Contact.getText().subSequence(9,holder.V_Contact.getText().length()));
                    con.startActivity(in);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView V_name,V_Contact,DateOfApp,TimeofApp;
        public ViewHolder(View itemView) {
            super(itemView);
            V_name=(TextView)itemView.findViewById(R.id.VisitorName);
            V_Contact=(TextView)itemView.findViewById(R.id.VisitorContact);
            DateOfApp=(TextView)itemView.findViewById(R.id.DateOfApp);
            TimeofApp=(TextView)itemView.findViewById(R.id.TimeOfApp);
        }
    }
}
