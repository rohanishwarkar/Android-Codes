/*
    Created By: Rohan Ishwarkar
 */
package com.example.razor.start.ADAPTERS;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.razor.start.MODELS.singlealert;
import com.example.razor.start.R;
import java.util.List;

public class AlertRecyclerAdapter extends RecyclerView.Adapter<AlertRecyclerAdapter.MyViewHolder> {

    List<singlealert> list;
    Context ctx;

    public AlertRecyclerAdapter(List<singlealert> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        singlealert m = list.get(i);
        viewHolder.title.setText(m.getTitle());
        viewHolder.message.setText(m.getMessage());
        viewHolder.time.setText(m.getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title,message,time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            message=itemView.findViewById(R.id.message);
            time=itemView.findViewById(R.id.time);
        }
    }
    public void add(List<singlealert> l){
        for(singlealert m:l){
            list.add(m);
        }
        notifyDataSetChanged();
    }
}