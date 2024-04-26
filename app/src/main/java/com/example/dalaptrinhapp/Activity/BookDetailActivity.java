package com.example.dalaptrinhapp.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.dalaptrinhapp.API.myAPI;
import com.example.dalaptrinhapp.Model.apiresponse;
import com.example.dalaptrinhapp.Model.bookDto;
import com.example.dalaptrinhapp.R;
import com.example.dalaptrinhapp.RetrofitManager.RetrofitManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookDetailActivity extends AppCompatActivity {
    private ImageView backicon,bookimage, homeicon;
    private TextView booktitle, bookcategory, bookauthor, bookdesc, bookprice;
    private myAPI myapi;
    private Button addtocartbtn;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookdetail);
        //xy ly add to cart/ backicon
        backicon = findViewById(R.id.backicon);
        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        homeicon = findViewById(R.id.homeicon);
        homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // hien thi dlieu
        booktitle = findViewById(R.id.booktitle);
        bookimage = findViewById(R.id.bookimage);
        bookcategory = findViewById(R.id.bookcategory);
        bookauthor = findViewById(R.id.bookauthor);
        bookdesc = findViewById(R.id.bookdesc);
        bookprice = findViewById(R.id.bookprice);

        SharedPreferences sharedPreferences = BookDetailActivity.this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int user_id = sharedPreferences.getInt("user_id", -1);

        Retrofit retrofit = RetrofitManager.getInstance();
        myapi = retrofit.create(myAPI.class);
        int book_id = getIntent().getIntExtra("book_id", -1);
        Log.d("cartuser_id", String.valueOf(user_id));
        Log.d("cartbook_id", String.valueOf(book_id));
        if (book_id != -1) {
            Call<bookDto> call = myapi.getBookwithCatename(book_id);
            call.enqueue(new Callback<bookDto>() {
                @Override
                public void onResponse(Call<bookDto> call, Response<bookDto> response) {
                    bookDto bookdtomodel = response.body();
                    booktitle.setText(bookdtomodel.getTitle());
                    bookcategory.setText(bookdtomodel.getCategory_name());
                    bookauthor.setText(bookdtomodel.getAuthor());
                    bookdesc.setText(bookdtomodel.getDescription());
                    bookprice.setText("$ " + String.valueOf(bookdtomodel.getPrice()));
                    Glide.with(getApplicationContext())
                            .load(bookdtomodel.getImage())
                            .error(R.drawable.notfound)
                            .into(bookimage);
                }
                @Override
                public void onFailure(Call<bookDto> call, Throwable throwable) {

                }
            });
        } else {
            Toast.makeText(this, "CAN NOT DEFINE BOOK", Toast.LENGTH_SHORT).show();
        }


//      btn add to cart
        addtocartbtn = findViewById(R.id.addtocartbtn);
        if (user_id != -1) {
            addtocartbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Call<apiresponse> call = myapi.addtoCart(user_id, book_id);
                        call.enqueue(new Callback<apiresponse>() {
                            @Override
                            public void onResponse(Call<apiresponse> call, Response<apiresponse> response) {
                                if (response.isSuccessful()) {
                                    apiresponse apiresponse = response.body();
                                    if (apiresponse.getStatus().equals("success")) {
                                        Toast.makeText(BookDetailActivity.this, apiresponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        // add false
                                        bookdesc.setText(apiresponse.getMessage());
//                                        Toast.makeText(BookDetailActivity.this, apiresponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(BookDetailActivity.this, "NO RESPONSE", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<apiresponse> call, Throwable throwable) {
                                bookdesc.setText(throwable.getMessage());

//                                Toast.makeText(BookDetailActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        } else {
            // chua dang nhap
            addtocartbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intenttocart = new Intent(BookDetailActivity.this, CartActivity.class);
                    startActivity(intenttocart);
                }
            });
        }
    }
}
