package com.example.dalaptrinhapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dalaptrinhapp.API.myAPI;
import com.example.dalaptrinhapp.Model.usermodel;
import com.example.dalaptrinhapp.R;
import com.example.dalaptrinhapp.RetrofitManager.RetrofitManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserSettingActivity extends AppCompatActivity {
    private ImageView backicon, toOrderHistory, toInforDetail, homeicon;
    private TextView username, email, login_txt, register_txt;
    private myAPI myapi;
    private ArrayList<usermodel> usermodels;
    private Button logoutbtn;
    private LinearLayout loggedLayout,intentoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

//        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.remove("user_id");
//        editor.apply();

        backicon = findViewById(R.id.backicon);
        toOrderHistory = findViewById(R.id.toOrderHistory);
        toInforDetail = findViewById(R.id.toInforDetail);
        username = findViewById(R.id.username);
        email = findViewById(R.id.useremail);
        loggedLayout = findViewById(R.id.loggedLayout);
        intentoLogin = findViewById(R.id.intentoLogin);
        homeicon = findViewById(R.id.homeicon);

        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttoHP = new Intent(UserSettingActivity.this, MainActivity.class);
                finish();
            }
        });

//         lay ra thong tin user
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int user_id = sharedPreferences.getInt("user_id", -1);

        Retrofit retrofit = RetrofitManager.getInstance();
        myapi = retrofit.create(myAPI.class);
        if(user_id != -1){
            // dang nhap roi
            intentoLogin.setVisibility(View.GONE);
            loggedLayout.setVisibility(View.VISIBLE);
            Call<usermodel> call = myapi.getUserbyUserID(user_id);
            call.enqueue(new Callback<usermodel>() {
                @Override
                public void onResponse(Call<usermodel> call, Response<usermodel> response) {
                        usermodel user = response.body();
                        username.setText(user.getUsername());
                        email.setText(user.getEmail());
                }

                @Override
                public void onFailure(Call<usermodel> call, Throwable throwable) {
                    System.out.println(throwable.getMessage());
                }
            });

            //intent to ìinfor detail
            toInforDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_to_Infordetail = new Intent(UserSettingActivity.this, UserInformationActivity.class);
                    startActivity(intent_to_Infordetail);
                }
            });

            toOrderHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_to_orderhistory = new Intent(UserSettingActivity.this, OrderListActivity.class);
                    startActivity(intent_to_orderhistory);
                }
            });

            //xu ly log out
            logoutbtn = findViewById(R.id.logoutbtn);
            logoutbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    //xoa user_id trong shared pref
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("user_id");
                    editor.apply();

                    // ve hp or ve login ?
                    Intent intent = new Intent(UserSettingActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        else{
            // hien view chua dang nhap
            loggedLayout.setVisibility(View.GONE);
            intentoLogin.setVisibility(View.VISIBLE);
            login_txt = findViewById(R.id.login_txt);
            register_txt = findViewById(R.id.register_txt);

            login_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentlogin = new Intent(UserSettingActivity.this, LoginActivity.class);
                    startActivity(intentlogin);
                }
            });

            register_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentregister = new Intent(UserSettingActivity.this, RegisterActivity.class);
                    startActivity(intentregister);
                }
            });
        }
    }
}

