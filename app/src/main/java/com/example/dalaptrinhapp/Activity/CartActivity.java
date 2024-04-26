package com.example.dalaptrinhapp.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dalaptrinhapp.API.myAPI;
import com.example.dalaptrinhapp.Adapter.myCartAdapter;
import com.example.dalaptrinhapp.Model.cartDto;
import com.example.dalaptrinhapp.R;
import com.example.dalaptrinhapp.RetrofitManager.RetrofitManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartActivity extends AppCompatActivity{
    private ImageView backicon,homeicon;
    private myAPI myapi;
    private ArrayList<cartDto> cartarrlist = new ArrayList<>();
    private myCartAdapter myadapter;
    private RecyclerView recyclerviewCartItem;
    private TextView totalprice, login_txt, register_txt, intentbooklist;
    private Button placeOrdbtn;
    private double total;
    private LinearLayout intentoLogin, footer, emptycart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int user_id = sharedPreferences.getInt("user_id", -1);
        Log.d("cartuserid", String.valueOf(user_id));

        recyclerviewCartItem=findViewById(R.id.recyclerviewCartItem);
        totalprice = findViewById(R.id.totalprice);
        intentbooklist = findViewById(R.id.intentbooklist);

        //back icon
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
                Intent intenttoHP = new Intent(CartActivity.this, MainActivity.class);
                startActivity(intenttoHP);
            }
        });

        intentbooklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttoblist = new Intent(CartActivity.this, BookListActivity.class);
                startActivity(intenttoblist);
            }
        });

//        lay ra list cart item
        Retrofit retrofit = RetrofitManager.getInstance();
        myapi = retrofit.create(myAPI.class);

        if (user_id != -1){
            // da dang nhap
            Call<ArrayList<cartDto>> call = myapi.getCartbyUserID(user_id);
            call.enqueue(new Callback<ArrayList<cartDto>>() {
                @Override
                public void onResponse(Call<ArrayList<cartDto>> call, Response<ArrayList<cartDto>> response) {
                    if (response.isSuccessful()) {
                        cartarrlist = response.body();
                        if (cartarrlist.size() == 0) {
                            //trong gio hang dang khong co san pham
                            emptycart = findViewById(R.id.emptycart);
                            emptycart.setVisibility(View.VISIBLE);
                        }
                        else {
                            for (int i = 0; i < cartarrlist.size(); i++) {
                                //call Adapter class
                                myadapter = new myCartAdapter(cartarrlist, CartActivity.this, totalprice);
                                LinearLayoutManager layout = new LinearLayoutManager(CartActivity.this, RecyclerView.VERTICAL, false);
                                recyclerviewCartItem.setLayoutManager(layout);
                                recyclerviewCartItem.setAdapter(myadapter);
                            }
                            total = caculateTotalPrice();
                            totalprice.setText(String.format("$ %.2f", total));
                        }
                        } else{
                        Toast.makeText(CartActivity.this, "NO RESPONSE", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ArrayList<cartDto>> call, Throwable throwable) {
                    Toast.makeText(CartActivity.this, "NO RESPONSE", Toast.LENGTH_SHORT).show();
                }
            });

            //btn placeord onclick
            placeOrdbtn= findViewById(R.id.placeOrdbtn);
            placeOrdbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intenttocheckout = new Intent(CartActivity.this, CheckoutActivity.class);
                    startActivity(intenttocheckout);
                }
            });
        } else {
            // xu ly khi người dùng chưa đăng nhập
            intentoLogin = findViewById(R.id.intentoLogin);
            footer = findViewById(R.id.footer);
            login_txt = findViewById(R.id.login_txt);
            register_txt = findViewById(R.id.register_txt);

            AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
            builder.setMessage("Please log in to add book to your cart");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            recyclerviewCartItem.setVisibility(View.GONE);
            footer.setVisibility(View.GONE);
            intentoLogin.setVisibility(View.VISIBLE);

            login_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentlogin = new Intent(CartActivity.this, LoginActivity.class);
                    startActivity(intentlogin);
                }
            });

            register_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentregister = new Intent(CartActivity.this, RegisterActivity.class);
                    startActivity(intentregister);
                }
            });
        }
    }

    private double caculateTotalPrice() {
        double total = 0.0;
        if (cartarrlist != null) {
            for (cartDto item : cartarrlist) {
                total += item.getBook_price() * item.getQuantity();
            }
        }
        return total;
    }

}