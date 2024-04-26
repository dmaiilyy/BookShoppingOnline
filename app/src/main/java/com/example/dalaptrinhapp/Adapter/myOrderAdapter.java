package com.example.dalaptrinhapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dalaptrinhapp.Activity.OrderDetailActivity;
import com.example.dalaptrinhapp.Activity.SupportActivity;
import com.example.dalaptrinhapp.Model.ordermodel;
import com.example.dalaptrinhapp.R;

import java.util.ArrayList;

public class myOrderAdapter extends RecyclerView.Adapter<myOrderAdapter.ViewHolder>{
    private ArrayList<ordermodel> orderlist;
    private Context context;

    public myOrderAdapter(final ArrayList<ordermodel> orderlist, final Context context) {
        this.orderlist = orderlist;
        this.context = context;
    }
//    public void setFilterlist(ArrayList<ordermodel> filterlist){
//        this.orderlist = filterlist;
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public myOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_historyoder_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ordermodel orderitem = orderlist.get(position);
        holder.orderID.setText("Order ID: " + orderitem.getOrder_id());
        holder.createddate.setText(orderitem.getCreatedate());
        holder.totalprice.setText(String.format("$ %.2f", orderitem.getTotal_price()));
        holder.quantity.setText("x " + String.valueOf(orderitem.getTotal_amount()));

        holder.viewdetailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int order_id = orderitem.getOrder_id();
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("order_id", order_id);
                Log.d("adtorderid", String.valueOf(order_id));
                context.startActivity(intent);
            }
        });

        holder.viewcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int order_id = orderitem.getOrder_id();
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("order_id", order_id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderID, createddate, totalprice,quantity;
        LinearLayout viewcontainer;
        Button  viewdetailbtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderID= itemView.findViewById(R.id.orderID);
            createddate= itemView.findViewById(R.id.createddate);
            totalprice= itemView.findViewById(R.id.totalprice);
            quantity= itemView.findViewById(R.id.quantity);
            viewdetailbtn= itemView.findViewById(R.id.vieworderdetail);
            viewcontainer= itemView.findViewById(R.id.container);
        }
    }
}
