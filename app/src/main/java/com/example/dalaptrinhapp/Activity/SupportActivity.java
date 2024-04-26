package com.example.dalaptrinhapp.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dalaptrinhapp.API.myAPI;
import com.example.dalaptrinhapp.Model.apiresponse;
import com.example.dalaptrinhapp.Model.usermodel;
import com.example.dalaptrinhapp.R;
import com.example.dalaptrinhapp.RetrofitManager.RetrofitManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SupportActivity extends AppCompatActivity {
    private ImageView backicon,homeicon;
    private Button sendbtn;
    private myAPI myapi;
    private TextView name, phone, message;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        message = findViewById(R.id.message);

        SharedPreferences sharedPreferences = SupportActivity.this.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int user_id = sharedPreferences.getInt("user_id", -1);

        Retrofit retrofit = RetrofitManager.getInstance();
        myapi = retrofit.create(myAPI.class);

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
                Intent intenttoHP = new Intent(SupportActivity.this, MainActivity.class);
                startActivity(intenttoHP);
            }
        });

        //lay ra dl nguoi dung
        if( user_id != -1)
        {
            Call<usermodel> call = myapi.getUserbyUserID(user_id);
            call.enqueue(new Callback<usermodel>() {
                @Override
                public void onResponse(Call<usermodel> call, Response<usermodel> response) {
                    if(response.isSuccessful()){
                        usermodel user = response.body();
                        name.setText(user.getUsername());
                        phone.setText(user.getPhonenumber());
                    } else {
                        Toast.makeText(SupportActivity.this, "NO RESPONSE", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<usermodel> call, Throwable throwable) {
                    Toast.makeText(SupportActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // chua dang nhap

        }


        //send btn
        sendbtn = findViewById(R.id.sendbtn);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cname = name.getText().toString().trim();
                String cphone = phone.getText().toString().trim();
                String cmessage = message.getText().toString().trim();
                if (cname.isEmpty()) {
                    name.setError("Please enter your name");
                    name.requestFocus();
                    return;
                }

                if (cphone.isEmpty()) {
                    phone.setError("Please enter your phone number");
                    phone.requestFocus();
                    return;
                }

                if (cmessage.isEmpty()) {
                    message.setError("Please enter your message");
                    message.requestFocus();
                    return;
                }

                // call api luu message vao csdl
                Call<apiresponse> call = myapi.sendmessage(user_id, cname, cphone, cmessage);
                call.enqueue(new Callback<apiresponse>() {
                    @Override
                    public void onResponse(Call<apiresponse> call, Response<apiresponse> response) {
                        if(response.isSuccessful()){
                            apiresponse apiresponse = response.body();
                            AlertDialog.Builder builder = new AlertDialog.Builder(SupportActivity.this);
                            builder.setMessage(apiresponse.getMessage());
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(SupportActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }else {
                            Toast.makeText(SupportActivity.this, "UNSUCESSFUL", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<apiresponse> call, Throwable throwable) {
                        Toast.makeText(SupportActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}