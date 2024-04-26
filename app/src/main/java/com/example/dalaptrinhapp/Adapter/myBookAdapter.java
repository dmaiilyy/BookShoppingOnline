package com.example.dalaptrinhapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dalaptrinhapp.API.myAPI;
import com.example.dalaptrinhapp.Activity.CartActivity;
import com.example.dalaptrinhapp.Model.apiresponse;
import com.example.dalaptrinhapp.Model.bookmodel;
import com.example.dalaptrinhapp.R;
import com.example.dalaptrinhapp.Activity.BookDetailActivity;
import com.example.dalaptrinhapp.RetrofitManager.RetrofitManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class myBookAdapter extends RecyclerView.Adapter<myBookAdapter.ViewHolder>{
    private ArrayList<bookmodel> bookarrList;
    private Context context;
    private myAPI myapi;

    public myBookAdapter(final ArrayList<bookmodel> bookarrList, final Context context) {
        this.bookarrList = bookarrList;
        this.context = context;
    }
    public void setFilterlist(ArrayList<bookmodel> filterlist){
        this.bookarrList = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public myBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_booklist_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        bookmodel bookModelmodel = bookarrList.get(position);
        holder.title.setText(bookModelmodel.getTitle());
        holder.author.setText(bookModelmodel.getAuthor());
        holder.price.setText("$ " + String.valueOf(bookModelmodel.getPrice()));

        //tv anh
        Glide.with(context)
                .load(bookModelmodel.getImage())
                .error(R.drawable.notfound)
                .into(holder.image);

        //
        holder.viewcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int book_id = bookModelmodel.getBook_id();
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("book_id", bookModelmodel.getBook_id());
                context.startActivity(intent);
            }
        });

        //xuly them vao gio hang
        holder.carticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                int user_id = sharedPreferences.getInt("user_id", -1);
                int book_id = bookModelmodel.getBook_id();

                Retrofit retrofit = RetrofitManager.getInstance();
                myapi = retrofit.create(myAPI.class);
                if (user_id != -1) {
                    Call<apiresponse> call = myapi.addtoCart(user_id, book_id);
                    call.enqueue(new Callback<apiresponse>() {
                        @Override
                        public void onResponse(Call<apiresponse> call, Response<apiresponse> response) {
                            if (response.isSuccessful()) {
                                apiresponse apiresponse = response.body();
                                Toast.makeText(context, apiresponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<apiresponse> call, Throwable throwable) {
                            Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });
                } else{
                    // chua dang nhap
                    Intent intenttocart = new Intent(context, CartActivity.class);
                    context.startActivity(intenttocart);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookarrList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,author, price;
        ImageView image,carticon;
        LinearLayout viewcontainer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title= itemView.findViewById(R.id.title);
            author= itemView.findViewById(R.id.author);
            price= itemView.findViewById(R.id.price);
            image= itemView.findViewById(R.id.image);
            carticon= itemView.findViewById(R.id.carticon);
            viewcontainer= itemView.findViewById(R.id.container);
        }
    }
}
