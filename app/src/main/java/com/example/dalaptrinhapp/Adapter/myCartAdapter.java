package com.example.dalaptrinhapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import com.example.dalaptrinhapp.Model.apiresponse;
import com.example.dalaptrinhapp.Model.cartDto;
import com.example.dalaptrinhapp.R;
import com.example.dalaptrinhapp.RetrofitManager.RetrofitManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class myCartAdapter extends RecyclerView.Adapter<myCartAdapter.ViewHolder>{
    private ArrayList<cartDto> cartarrlist;
    private Context context;
    private TextView totalprice;
    public myCartAdapter(final ArrayList<cartDto> cartarrlist, final Context context, final TextView totalprice) {
        this.cartarrlist = cartarrlist;
        this.context = context;
        this.totalprice = totalprice;
    }

    @NonNull
    @Override
    public myCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_cart_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myCartAdapter.ViewHolder holder, int position) {
        cartDto cartDtomodel = cartarrlist.get(position);
        holder.title.setText(cartDtomodel.getBook_title());
        holder.author.setText(cartDtomodel.getBook_author());
        holder.price.setText(String.valueOf("$ " +cartDtomodel.getBook_price()));
        Glide.with(context)
                .load(cartDtomodel.getBook_image())
                .error(R.drawable.notfound)
                .into(holder.image);
        holder.quantity.setText(String.valueOf(cartDtomodel.getQuantity()));

        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", context.MODE_PRIVATE);
        int user_id = sharedPreferences.getInt("user_id", -1);

        //tang giam sluong book
        //diff
        Retrofit retrofit = RetrofitManager.getInstance();
        myAPI myapi = retrofit.create(myAPI.class);
        holder.diff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = cartDtomodel.getQuantity() -1;
                if (newQuantity > 0 ){
                    Call<apiresponse> call = myapi.diffQuantity(user_id, cartDtomodel.getBook_id());
                    call.enqueue(new Callback<apiresponse>() {
                        @Override
                        public void onResponse(Call<apiresponse> call, Response<apiresponse> response) {
                            if (response.isSuccessful()) {
                                notifyDataSetChanged();
                                apiresponse apiresponse = response.body();
                            } else {

                            }
                        }

                        @Override
                        public void onFailure(Call<apiresponse> call, Throwable throwable) {
                        }
                    });
                    cartDtomodel.setQuantity(newQuantity);
                    notifyItemChanged(position);
                    caculateTotalPrice();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure to delete this item from your cart?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Call<apiresponse> call = myapi.deleteCartItem(user_id, cartDtomodel.getBook_id());
                            call.enqueue(new Callback<apiresponse>() {
                                @Override
                                public void onResponse(Call<apiresponse> call, Response<apiresponse> response) {
                                    if (response.isSuccessful()) {
                                        apiresponse apiresponse = response.body();
                                        Toast.makeText(context, apiresponse.getMessage(), Toast.LENGTH_SHORT).show();
                                        cartarrlist.remove(position);
                                        notifyItemRemoved(position);
                                        notifyDataSetChanged();
                                        caculateTotalPrice();
                                    } else {
                                        Toast.makeText(context, "NO RESPONSE", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<apiresponse> call, Throwable throwable) {

                                }
                            });
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        //plus
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = cartDtomodel.getQuantity() + 1;
                Retrofit retrofit = RetrofitManager.getInstance();
                myAPI myapi = retrofit.create(myAPI.class);
                Call<apiresponse> call = myapi.plusQuantity(user_id, cartDtomodel.getBook_id());
                call.enqueue(new Callback<apiresponse>() {
                    @Override
                    public void onResponse(Call<apiresponse> call, Response<apiresponse> response) {
                        if (response.isSuccessful()) {
                            notifyDataSetChanged();
                            apiresponse apiresponse = response.body();
                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<apiresponse> call, Throwable throwable) {
                    }
                });
                cartDtomodel.setQuantity(newQuantity);
                notifyItemChanged(position);
                caculateTotalPrice();
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartarrlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, author, price, quantity,diff,plus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            //
            diff = itemView.findViewById(R.id.diff);
            plus = itemView.findViewById(R.id.plus);
        }
    }

    private void caculateTotalPrice() {
        double total = 0.0;
        if (cartarrlist != null) {
            for (cartDto item : cartarrlist) {
                total += item.getBook_price() * item.getQuantity();
            }
        }
        totalprice.setText(String.format("$ %.2f", total));
    }
}

