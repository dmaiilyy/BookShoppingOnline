package com.example.dalaptrinhapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    private CheckBox cbshowpw;

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
                Intent toregister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(toregister);
            }
        });

        //xu ly login
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginbtn = findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtemail = email.getText().toString();
                Log.d("email", txtemail);
                String txtpw = password.getText().toString();

                if (txtemail.isEmpty()) {
                    email.setError("Please enter your email");
                    email.requestFocus();
                    return;
                }

                if (txtpw.isEmpty()) {
                    password.setError("Please enter a password");
                    password.requestFocus();
                    return;
                }

                Retrofit retrofit = RetrofitManager.getInstance();
                myapi = retrofit.create(myAPI.class);

                Call<apiresponse> call = myapi.authenticate(txtemail, txtpw);
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
                                Log.d("loginuser_id", String.valueOf(user_id));
                                editor.commit();

                                Intent intent_to_hp = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent_to_hp);
                            } else if(loginResponse.getStatus().equals("incorectpw")) {
                                password.setError(loginResponse.getMessage());
                                password.requestFocus();
                            } else if(loginResponse.getStatus().equals("emailexist")) {
                                email.setError(loginResponse.getMessage());
                                email.requestFocus();
                            }
                        } else {
                                Toast.makeText(getApplicationContext(), "NO RESPONSE", Toast.LENGTH_SHORT ).show();
                        }
                    }
                        @Override
                        public void onFailure(Call<apiresponse> call, Throwable throwable) {
                            Toast.makeText(LoginActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
        });

        //checkbox hien pw
        cbshowpw= findViewById(R.id.cbshowpw);
        cbshowpw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Nếu checkbox được chọn, hiển thị mật khẩu
                    password.setTransformationMethod(null);
                } else {
                    // Nếu checkbox không được chọn, ẩn mật khẩu
                    password.setTransformationMethod(new PasswordTransformationMethod()); // Ẩn mật khẩu
                }
            }
        });

        //dong ban phim ao
        ViewGroup parentLayout = findViewById(R.id.parentLayout);
        parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    boolean isTouchedOutsideEditText = true;
                    for (int i = 0; i < parentLayout.getChildCount(); i++) {
                        View childView = parentLayout.getChildAt(i);
                        if (childView instanceof EditText) {
                            Rect outRect = new Rect();
                            childView.getGlobalVisibleRect(outRect);
                            if (outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                                isTouchedOutsideEditText = false;
                                break;
                            }
                        }
                    }
                    if (isTouchedOutsideEditText) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(parentLayout.getWindowToken(), 0);
                        parentLayout.requestFocus();
                    }
                }
                return false;
            }
        });



    }
}