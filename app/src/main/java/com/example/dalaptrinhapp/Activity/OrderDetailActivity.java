package com.example.dalaptrinhapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dalaptrinhapp.API.myAPI;
import com.example.dalaptrinhapp.Adapter.myOrderItemAdapter;
import com.example.dalaptrinhapp.Model.orderdetailDto;
import com.example.dalaptrinhapp.Model.ordermodel;
import com.example.dalaptrinhapp.R;
import com.example.dalaptrinhapp.RetrofitManager.RetrofitManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderDetailActivity extends AppCompatActivity {
    private TextView orderid, username, phonenumber, email,
            address,createdate, totalquantity, subtotal, totalprice;
    private RecyclerView recyclerviewOrderItem;
    private ImageView backicon, homeicon;
    private myOrderItemAdapter myadapter;
    private ArrayList<orderdetailDto> odlist = new ArrayList<>();
    private myAPI myapi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orderid = findViewById(R.id.orderid);
        username = findViewById(R.id.username);
        phonenumber = findViewById(R.id.phonenumber);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        createdate = findViewById(R.id.createdate);
        totalquantity = findViewById(R.id.totalquantity);
        subtotal = findViewById(R.id.subtotal);
        totalprice = findViewById(R.id.totalprice);
        recyclerviewOrderItem = findViewById(R.id.recyclerviewOrderItem);
        backicon = findViewById(R.id.backicon);

        SharedPreferences sharedPreferences = OrderDetailActivity.this.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int user_id = sharedPreferences.getInt("user_id", -1);
        Log.d("ordetailuid", String.valueOf(user_id));
        int order_id = getIntent().getIntExtra("order_id", -1);
        Log.d("ordid" , String.valueOf(order_id));
//        //backicon
        backicon = findViewById(R.id.backicon);
        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//
        homeicon = findViewById(R.id.homeicon);
        homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttoHP = new Intent(OrderDetailActivity.this, MainActivity.class);
                startActivity(intenttoHP);
            }
        });

        //call api
        Retrofit retrofit = RetrofitManager.getInstance();
        myapi = retrofit.create(myAPI.class);
        Call<ArrayList<orderdetailDto>> call = myapi.getOrderDetail(2, order_id);
        call.enqueue(new Callback<ArrayList<orderdetailDto>>() {
            @Override
            public void onResponse(Call<ArrayList<orderdetailDto>> call, Response<ArrayList<orderdetailDto>> response) {
                if(response.isSuccessful()){
                    odlist =response.body();
                    for (int i = 0; i < odlist.size(); i ++){
                        myadapter = new myOrderItemAdapter(odlist, OrderDetailActivity.this);
                        LinearLayoutManager layout = new LinearLayoutManager(OrderDetailActivity.this,RecyclerView.VERTICAL, false);
                        recyclerviewOrderItem.setLayoutManager(layout);
                        recyclerviewOrderItem.setAdapter(myadapter);
                    }
                } else {
                    Toast.makeText(OrderDetailActivity.this, "NO RESPONSE", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<orderdetailDto>> call, Throwable throwable) {
                Toast.makeText(OrderDetailActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // lay ra thong tin don
        Call<ordermodel> callorder = myapi.getOrderbyOrderID(order_id);
        callorder.enqueue(new Callback<ordermodel>() {
            @Override
            public void onResponse(Call<ordermodel> call, Response<ordermodel> response) {
                if(response.isSuccessful()){
                    ordermodel omodel = response.body();
                    orderid.setText("Order ID: " + omodel.getOrder_id());
                    username.setText("Name: " + omodel.getUser_name());
                    email.setText("Name: " + omodel.getUser_email());
                    phonenumber.setText("Phone number: " + omodel.getUser_phone());
                    address.setText("Address: " + omodel.getUser_add());
                    createdate.setText("Created at: " + omodel.getCreatedate());
                    totalquantity.setText(String.valueOf(omodel.getTotal_amount()));
                    subtotal.setText("$ " + String.valueOf(omodel.getTotal_price()-5));
                    Log.d("totalprice", String.valueOf(omodel.getTotal_price()));
                    Log.d("totalamount", String.valueOf(omodel.getTotal_amount()));
                    totalprice.setText("$ " + String.valueOf(omodel.getTotal_price()));
                } else {
                    Toast.makeText(OrderDetailActivity.this, "NO RESPONSE", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ordermodel> call, Throwable throwable) {
                Toast.makeText(OrderDetailActivity.this, "NO RESPONSE", Toast.LENGTH_SHORT).show();
            }
        });


    }
}