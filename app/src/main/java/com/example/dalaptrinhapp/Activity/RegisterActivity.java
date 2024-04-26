package com.example.dalaptrinhapp.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {

    private TextView lgtxt;
    private EditText name, phone, email, pw, cpw;
    private ImageView backicon;
    private Button registerbtn;
    private myAPI myapi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerform);

        backicon = findViewById(R.id.backicon);
        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lgtxt= findViewById(R.id.login_text);
        lgtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(myintent);
            }
        });

        //xu ly dang ky
        registerbtn = findViewById(R.id.registerbtn);
        name= findViewById(R.id.name);
        phone= findViewById(R.id.phone);
        email= findViewById(R.id.email);
        pw= findViewById(R.id.pw);
        cpw= findViewById(R.id.cpw);
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cname = name.getText().toString().trim();
                String cemail = email.getText().toString().trim();
                String cphone = phone.getText().toString().trim();
                String checkpw = pw.getText().toString();
                String confirmpw = cpw.getText().toString();

                if (cname.isEmpty()) {
                    name.setError("Please enter your name");
                    name.requestFocus();
                    return;
                }

                if (cemail.isEmpty()) {
                    email.setError("Please enter your email");
                    email.requestFocus();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(cemail).matches()) {
                    email.setError("Please enter a valid email");
                    email.requestFocus();
                    return;
                }

                if (cphone.isEmpty()) {
                    phone.setError("Please enter your phone number");
                    phone.requestFocus();
                    return;
                }

                if (checkpw.isEmpty()) {
                    pw.setError("Please enter a password");
                    pw.requestFocus();
                    return;
                }

                if (confirmpw.isEmpty()) {
                    cpw.setError("Please confirm your password");
                    cpw.requestFocus();
                    return;
                }

                if (!checkpw.equals(confirmpw)) {
                    cpw.setError("Passwords do not match");
                    cpw.requestFocus();
                    return;
                }

                if (checkpw.length() < 6) {
                    pw.setError("Password must be at least 6 characters long");
                    pw.requestFocus();
                    return;
                }

                Retrofit retrofit = RetrofitManager.getInstance();
                myapi = retrofit.create(myAPI.class);
                Call<apiresponse> call = myapi.register(cname, cemail,cphone, checkpw,confirmpw);
                call.enqueue(new Callback<apiresponse>() {
                    @Override
                    public void onResponse(Call<apiresponse> call, Response<apiresponse> response) {
                        if (response.isSuccessful()) {
                            apiresponse apiresponse = response.body();
                            if (apiresponse.getStatus().equals("success")) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage(apiresponse.getMessage());
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        Intent intent_to_login = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent_to_login);
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();


                            } else {
                                Toast.makeText(getApplicationContext(), apiresponse.getMessage(), Toast.LENGTH_SHORT ).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Dang ky khong thanh cong", Toast.LENGTH_SHORT ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<apiresponse> call, Throwable throwable) {

                    }
                });

            }
        });






    }
}