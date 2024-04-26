package com.example.dalaptrinhapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dalaptrinhapp.API.myAPI;
import com.example.dalaptrinhapp.Model.apiresponse;
import com.example.dalaptrinhapp.R;
import com.example.dalaptrinhapp.RetrofitManager.RetrofitManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private TextView rgtext;
    private ImageView backicon;
    private EditText email;
    private EditText password;
    private Button loginbtn;
    private myAPI myapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginform);

        backicon = findViewById(R.id.backicon);
        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //intent to login
        rgtext= findViewById(R.id.register_text);
        rgtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_to_login = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent_to_login);
            }
        });

        //xu ly login
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginbtn = findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cemail = email.getText().toString();
                String cpassword = password.getText().toString();

                if (cemail.isEmpty()) {
                    email.setError("Please enter your email");
                    email.requestFocus();
                    return;
                }

                if (cpassword.isEmpty()) {
                    password.setError("Please enter a password");
                    password.requestFocus();
                    return;
                }

                Retrofit retrofit = RetrofitManager.getInstance();
                myapi = retrofit.create(myAPI.class);

                Call<apiresponse> call = myapi.authenticate(cemail, cpassword);
                call.enqueue(new Callback<apiresponse>() {
                    @Override
                    public void onResponse(Call<apiresponse> call, Response<apiresponse> response) {
                        if (response.isSuccessful()) {
                            apiresponse loginResponse = response.body();
                            if (loginResponse.getStatus().equals("success")) {

                                int user_id = loginResponse.getUser_id();

                                //luu user_id vao shared preference de ti truyen len url
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("user_id", user_id);
                                editor.commit();

                                Intent intent_to_hp = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent_to_hp);
                            } else {
                                Toast.makeText(getApplicationContext(), loginResponse.getMessage(), Toast.LENGTH_SHORT ).show();
                            }
                        } else {
                                Toast.makeText(getApplicationContext(), "Dang nhap khong thanh cong", Toast.LENGTH_SHORT ).show();
                        }
                    }
                        @Override
                        public void onFailure(Call<apiresponse> call, Throwable throwable) {
                            Toast.makeText(LoginActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }
        });


    }
}