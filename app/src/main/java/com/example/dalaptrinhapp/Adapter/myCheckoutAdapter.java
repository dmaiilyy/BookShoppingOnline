package com.example.dalaptrinhapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dalaptrinhapp.API.myAPI;
import com.example.dalaptrinhapp.Activity.CheckoutActivity;
import com.example.dalaptrinhapp.Model.apiresponse;
import com.example.dalaptrinhapp.Model.cartDto;
import com.example.dalaptrinhapp.Model.orderdetailDto;
import com.example.dalaptrinhapp.R;
import com.example.dalaptrinhapp.RetrofitManager.RetrofitManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class myCheckoutAdapter extends RecyclerView.Adapter<myCheckoutAdapter.ViewHolder>{
    private ArrayList<cartDto> cartarrlist;
    private Context context;
    public myCheckoutAdapter(final ArrayList<cartDto> cartarrlist, final Context context) {
        this.cartarrlist = cartarrlist;
        this.context = context;
    }

    @NonNull
    @Override
    public myCheckoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_checkout_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myCheckoutAdapter.ViewHolder holder, int position) {
        cartDto cartDtomodel = cartarrlist.get(position);
        holder.title.setText(cartDtomodel.getBook_title());
        holder.author.setText(cartDtomodel.getBook_author());
        holder.price.setText(String.valueOf("$ " +cartDtomodel.getBook_price()*cartDtomodel.getQuantity()));
        Glide.with(context)
                .load(cartDtomodel.getBook_image())
                .error(R.drawable.notfound)
                .into(holder.image);
        holder.quantity.setText(String.valueOf("x " + cartDtomodel.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return cartarrlist.size();
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

