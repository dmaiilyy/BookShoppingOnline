package com.example.dalaptrinhapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dalaptrinhapp.Fragment.homepageFragment;
import com.example.dalaptrinhapp.Model.bookmodel;
import com.example.dalaptrinhapp.R;
import com.example.dalaptrinhapp.Activity.BookDetailActivity;

import java.util.ArrayList;

public class myBookHPAdapter extends RecyclerView.Adapter<myBookHPAdapter.ViewHolder>{
    private ArrayList<bookmodel> bookarrList;
    private Context context;

    public myBookHPAdapter(final ArrayList<bookmodel> bookarrList, final Context context) {
        this.bookarrList = bookarrList;
        this.context = context;
    }

    @NonNull
    @Override
    public myBookHPAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_book_homepage, parent,false);
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
//                context.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookarrList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, author, price;
        ImageView image;
        LinearLayout viewcontainer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title= itemView.findViewById(R.id.title);
            author= itemView.findViewById(R.id.author);
            price= itemView.findViewById(R.id.price);
            image= itemView.findViewById(R.id.image);
            viewcontainer= itemView.findViewById(R.id.container);
        }

    }
}
