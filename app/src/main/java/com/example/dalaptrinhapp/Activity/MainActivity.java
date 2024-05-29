package com.example.dalaptrinhapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dalaptrinhapp.API.myAPI;
import com.example.dalaptrinhapp.Adapter.myBookHPAdapter;
import com.example.dalaptrinhapp.Model.bookmodel;
import com.example.dalaptrinhapp.R;
import com.example.dalaptrinhapp.RetrofitManager.RetrofitManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewpager;
    private BottomNavigationView btnav;
    private TextView viewallbook, intentlogin;
    private RecyclerView homepagerccv;
    private ArrayList<bookmodel> bookmodelarraylist;
    private myAPI mybookapi;
    private myBookHPAdapter myhpadapter;
    private LinearLayout loginlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginlayout = findViewById(R.id.loginlayout);

        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int user_id = sharedPreferences.getInt("user_id", -1);

        if(user_id != -1){
            // da dang nhap
            loginlayout.setVisibility(View.GONE);
            Log.d("hpuser_id", String.valueOf(user_id));
        } else{
            //chua dang nhap
            loginlayout.setVisibility(View.VISIBLE);
            intentlogin = findViewById(R.id.intentlogin);
            intentlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intenttologin = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intenttologin);
                }
            });
        }

        viewallbook = findViewById(R.id.viewallbook);
        viewallbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_to_booklist = new Intent(MainActivity.this, BookListActivity.class);
                startActivity(intent_to_booklist);
            }
        });

        //lay ra list new books
        homepagerccv = findViewById(R.id.homepagerccv);

        bookmodelarraylist = new ArrayList<>();
        Retrofit retrofit = RetrofitManager.getInstance();
        mybookapi = retrofit.create(myAPI.class);

        Call<ArrayList<bookmodel>> newBooklist = mybookapi.callNewBooklist();
        newBooklist.enqueue(new Callback<ArrayList<bookmodel>>() {
            @Override
            public void onResponse(Call<ArrayList<bookmodel>> call, Response<ArrayList<bookmodel>> response) {
                bookmodelarraylist = response.body();
                    myhpadapter = new myBookHPAdapter(bookmodelarraylist, MainActivity.this);
                    LinearLayoutManager layout = new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL,false);
                    homepagerccv.setLayoutManager(layout);
                    homepagerccv.setAdapter(myhpadapter);
            }

            @Override
            public void onFailure(Call<ArrayList<bookmodel>> call, Throwable throwable) {
                Toast.makeText(MainActivity.this, "retrofit Error", Toast.LENGTH_SHORT);
            }
        });

        btnav = findViewById(R.id.btnavigation);

        btnav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.homepage) {
                    return item.getItemId() == R.id.homepage;
                }else if(item.getItemId() == R.id.cart) {
                    Intent intent_to_cart = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(intent_to_cart);
                    return item.getItemId() == R.id.homepage;
                }else if (item.getItemId() == R.id.booklist) {
                    Intent intent_to_booklist = new Intent(MainActivity.this, BookListActivity.class);
                    startActivity(intent_to_booklist);
                    return item.getItemId() == R.id.homepage;
                }else if (item.getItemId() == R.id.user) {
                    Intent intent_to_usersetting = new Intent(MainActivity.this, UserSettingActivity.class);
                    startActivity(intent_to_usersetting);
                    return item.getItemId() == R.id.homepage;
                }else if (item.getItemId() == R.id.support) {
                    Intent intent_to_support = new Intent(MainActivity.this, SupportActivity.class);
                    startActivity(intent_to_support);
                    return item.getItemId() == R.id.homepage;
                }
                return true;
            }
        });
    }
}
