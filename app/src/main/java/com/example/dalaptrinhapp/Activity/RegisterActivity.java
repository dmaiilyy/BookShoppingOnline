package com.example.dalaptrinhapp.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
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

public class RegisterActivity extends AppCompatActivity {
    private TextView lgtxt;
    private EditText name, phone, email, pw, cpw;
    private ImageView backicon;
    private Button registerbtn;
    private myAPI myapi;
    private CheckBox cbshowcpw, cbshowpw;

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

                if (!isValidUsername(cname)) {
                    name.setError("Username must only contain letters (uppercase or lowercase) and spaces!");
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

                if (!isValidPhoneNumber(cphone)) {
                    phone.setError("Please enter a valid phone number (starting with 0 and have 10 digits)");
                    phone.requestFocus();
                    return;
                }

                if (!isValidPassword(checkpw)) {
                    pw.setError("Password must contain at least one letter and one digit!");
                    pw.requestFocus();
                    return;
                }

                if (checkpw.isEmpty()) {
                    pw.setError("Please enter your password");
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
                Call<apiresponse> call = myapi.register(cname, cemail, cphone, checkpw);
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
                            Toast.makeText(getApplicationContext(), "NO SUCCESSFUL", Toast.LENGTH_SHORT ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<apiresponse> call, Throwable throwable) {
                    }
                });

            }
        });

        //xu ly show pw
        cbshowcpw = findViewById(R.id.cbshowcpw);
        cbshowpw = findViewById(R.id.cbshowpw);
        cbshowpw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Nếu checkbox được chọn, hiển thị mật khẩu
                    pw.setTransformationMethod(null);
                } else {
                    // Nếu checkbox không được chọn, ẩn mật khẩu
                    pw.setTransformationMethod(new PasswordTransformationMethod()); // Ẩn mật khẩu
                }
            }
        });

        cbshowcpw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Nếu checkbox được chọn, hiển thị mật khẩu
                    cpw.setTransformationMethod(null);
                } else {
                    // Nếu checkbox không được chọn, ẩn mật khẩu
                    cpw.setTransformationMethod(new PasswordTransformationMethod()); // Ẩn mật khẩu
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
    public boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "/(84|0[3|5|7|8|9])+([0-9]{8})\\b";
        return phoneNumber.matches(regex);
    }

    public boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{2,}$";
        return password.matches(regex);
    }

    public boolean isValidUsername(String username) {
        String regex = "^[a-zA-Z ]+$";
        return username.matches(regex);
    }
}