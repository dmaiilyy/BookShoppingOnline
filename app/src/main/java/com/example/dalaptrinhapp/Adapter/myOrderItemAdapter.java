package com.example.dalaptrinhapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dalaptrinhapp.Model.orderdetailDto;
import com.example.dalaptrinhapp.R;

import java.util.ArrayList;

public class myOrderItemAdapter extends RecyclerView.Adapter<myOrderItemAdapter.ViewHolder>{
    private ArrayList<orderdetailDto> odlist;
    private Context context;
    public myOrderItemAdapter(final ArrayList<orderdetailDto> odlist, final Context context) {
        this.odlist = odlist;
        this.context = context;
    }

    @NonNull
    @Override
    public myOrderItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_checkout_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myOrderItemAdapter.ViewHolder holder, int position) {
        orderdetailDto odmodel = odlist.get(position);
        holder.title.setText(odmodel.getTitle());
        holder.author.setText(odmodel.getAuthor());
        holder.price.setText(String.valueOf("$ " +odmodel.getUnitprice()*odmodel.getQuantity()));
        Glide.with(context)
                .load(odmodel.getImage())
                .error(R.drawable.notfound)
                .into(holder.image);
        holder.quantity.setText(String.valueOf("x " + odmodel.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return odlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, author, price, quantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }

}

