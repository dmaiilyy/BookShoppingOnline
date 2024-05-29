package com.example.dalaptrinhapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.content.SharedPreferences;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.dalaptrinhapp.API.myAPI;
import com.example.dalaptrinhapp.Adapter.myOrderAdapter;
import com.example.dalaptrinhapp.Model.bookmodel;
import com.example.dalaptrinhapp.Model.ordermodel;
import com.example.dalaptrinhapp.R;
import com.example.dalaptrinhapp.RetrofitManager.RetrofitManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderListActivity extends AppCompatActivity {
    private TextView toshopping;
    private myAPI myapi;
    private ArrayList<ordermodel> ordermodelarraylist = new ArrayList<>();
    private myOrderAdapter myadapter;
    private RecyclerView recyclerviewOrderItem;
    private ImageView backicon,homeicon;
    private SearchView searchview;
    private LinearLayout gotoshopping;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        recyclerviewOrderItem = findViewById(R.id.recyclerviewOrderItem);
        backicon = findViewById(R.id.backicon);
//        searchview = findViewById(R.id.searchview);

        SharedPreferences sharedPreferences = OrderListActivity.this.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int user_id = sharedPreferences.getInt("user_id", -1);
        Log.d("orduid", String.valueOf(user_id));

        //backicon
        backicon = findViewById(R.id.backicon);
        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //homeicon
        homeicon = findViewById(R.id.homeicon);
        homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttoHP = new Intent(OrderListActivity.this, MainActivity.class);
                startActivity(intenttoHP);
            }
        });

        //searchview
        searchview = findViewById(R.id.searchview);
        searchview.clearFocus();
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        // to shopping
        toshopping =findViewById(R.id.toshopping);
        toshopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tobooklist = new Intent(OrderListActivity.this, BookListActivity.class);
                startActivity(tobooklist);
            }
        });

        //lay ra dsach order
        Retrofit retrofit = RetrofitManager.getInstance();
        myapi = retrofit.create(myAPI.class);

        Call<ArrayList<ordermodel>> call = myapi.getOrderListbyuID(user_id);
        call.enqueue(new Callback<ArrayList<ordermodel>>() {
            @Override
            public void onResponse(Call<ArrayList<ordermodel>> call, Response<ArrayList<ordermodel>> response) {
                if(response.isSuccessful()){
                    ordermodelarraylist = response.body();
                    if (ordermodelarraylist.size() == 0) {
                        gotoshopping = findViewById(R.id.gotoshopping);
                        gotoshopping.setVisibility(View.VISIBLE);
                        recyclerviewOrderItem.setVisibility(View.GONE);
                    } else {
                        for (int i = 0; i < ordermodelarraylist.size(); i++) {
                            //call Adapter class
                            myadapter = new myOrderAdapter(ordermodelarraylist, OrderListActivity.this);
                            LinearLayoutManager layout = new LinearLayoutManager(OrderListActivity.this, RecyclerView.VERTICAL, false);
                            recyclerviewOrderItem.setLayoutManager(layout);
                            recyclerviewOrderItem.setAdapter(myadapter);
                        }
                    }
                } else {
                    Toast.makeText(OrderListActivity.this, "RESPONSE UNSUCCESSFUL", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ordermodel>> call, Throwable throwable) {
                Toast.makeText(OrderListActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void filterList(String text) {
        ArrayList<ordermodel> filterlist = new ArrayList<>();
        if(isNumber(text)) {
            Log.d("stext", text);
            //tim kiem theo order id
            int orderId = Integer.parseInt(text);
            for (ordermodel item : ordermodelarraylist) {
//                Log.d("createddate", item.getCreated());
                if (item.getOrder_id() == orderId) {
                    filterlist.add(item);
                }
            }

        } else {
            Toast.makeText(this, "data not found", Toast.LENGTH_SHORT).show();
        }
        if (filterlist.isEmpty()){
            myadapter.setFilterlist(filterlist);
//            emptynotify.setVisibility(View.VISIBLE);
        } else{
//            emptynotify.setVisibility(View.GONE);
            myadapter.setFilterlist(filterlist);
        }
    }

    public boolean isNumber(String txt) {
        String regex = "^[0-9]+$";
        return txt.matches(regex);
    }
    public boolean isDate(String txt) {
        String regex = "^\\d{2}-\\d{2}-\\d{4}$";
        return txt.matches(regex);
    }
}