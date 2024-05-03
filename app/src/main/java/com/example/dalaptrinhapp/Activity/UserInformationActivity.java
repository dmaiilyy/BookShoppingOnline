package com.example.dalaptrinhapp.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dalaptrinhapp.API.myAPI;
import com.example.dalaptrinhapp.Model.apiresponse;
import com.example.dalaptrinhapp.Model.usermodel;
import com.example.dalaptrinhapp.R;
import com.example.dalaptrinhapp.RetrofitManager.RetrofitManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserInformationActivity extends AppCompatActivity {
    private EditText name, phone, address;
    private TextView email;
    private ImageView backicon;
    private Button updatebtn;
    private myAPI myapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_infor);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        backicon = findViewById(R.id.backicon);
        updatebtn = findViewById(R.id.updatebtn);

        //backicon
        backicon = findViewById(R.id.backicon);
        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences sharedPreferences = UserInformationActivity.this.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int user_id = sharedPreferences.getInt("user_id", -1);


        Retrofit retrofit = RetrofitManager.getInstance();
        myapi = retrofit.create(myAPI.class);

        //hien thong tin len form
        Call<usermodel> call = myapi.getUserbyUserID(user_id);
        call.enqueue(new Callback<usermodel>() {
            @Override
            public void onResponse(Call<usermodel> call, Response<usermodel> response) {
                if( response.isSuccessful()){
                    usermodel user = response.body();
                    name.setText(user.getUsername());
                    email.setText(user.getEmail());
                    phone.setText(user.getPhonenumber());
                    if(user.getAddress() == null){
                        address.setHint("Enter Your Address ...");
                    } else{
                        address.setText(user.getAddress());
                    }
                }
            }

            @Override
            public void onFailure(Call<usermodel> call, Throwable throwable) {

            }
        });

        //khi bam vao email thi hien tbao
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserInformationActivity.this);
                builder.setMessage("CAN NOT CHANGE YOUR EMAIL");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //update
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cname = name.getText().toString().trim();
                String cphone = phone.getText().toString().trim();
                String caddress = address.getText().toString().trim();
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

                if(!isValidPhoneNumber(cphone)){
                    phone.setError("Please enter correct phone number form");
                    phone.requestFocus();
                    return;
                }

                if (caddress.isEmpty()) {
                    address.setError("Please enter your address");
                    address.requestFocus();
                    return;
                }
//
//                if (isValidAdd(caddress)) {
//                    address.setError("Add");
//                    address.requestFocus();
//                    return;
//                }

                //viet api update userinfor
                Call<apiresponse> call = myapi.updateUserInfor(user_id, cname, cphone,caddress);
                call.enqueue(new Callback<apiresponse>() {
                    @Override
                    public void onResponse(Call<apiresponse> call, Response<apiresponse> response) {
                        if(response.isSuccessful()){
                            apiresponse apiresponse = response.body();
                            Toast.makeText(UserInformationActivity.this, apiresponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(UserInformationActivity.this, "NO RESPONSE", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<apiresponse> call, Throwable throwable) {
                        Toast.makeText(UserInformationActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
    public static boolean isValidPhoneNumber(String phonenumber) {
        String phoneRegex = "^(84|0[3|5|7|8|9])+([0-9]{8})";
        return phonenumber.matches(phoneRegex);
    }

//    public boolean isValidAdd(String txt) {
//        String regex = "^[a-zA-Z ]+$";
//        return txt.matches(regex);
//    }

}