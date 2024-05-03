package com.example.dalaptrinhapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AlertDialog;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dalaptrinhapp.API.myAPI;
import com.example.dalaptrinhapp.Adapter.myCheckoutAdapter;
import com.example.dalaptrinhapp.Model.apiresponse;
import com.example.dalaptrinhapp.Model.cartDto;
import com.example.dalaptrinhapp.Model.usermodel;
import com.example.dalaptrinhapp.R;
import com.example.dalaptrinhapp.RetrofitManager.RetrofitManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CheckoutActivity extends AppCompatActivity {
    private ImageView iconeditDeliInfor;
    private LinearLayout inputDeliInfor;
    private boolean isOpen = false;
    private EditText name, phone, address,email;
    private myAPI myapi;
    private ImageView backicon;
    private TextView totalbookprice, totalprice;
    private ArrayList<cartDto> cartarrlist = new ArrayList<>();
    private myCheckoutAdapter myadapter;
    private RecyclerView rccvcheckoutitem;
    private Button placeOrdbtn;
    private int totalQuantity=0;
    private double subtotal = 0, totalbprice= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        iconeditDeliInfor = findViewById(R.id.editDeliInfor);
        inputDeliInfor = findViewById(R.id.inputDeliInfor);
        rccvcheckoutitem = findViewById(R.id.rccvcheckoutitem);
        placeOrdbtn = findViewById(R.id.placeOrdbtn);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        backicon = findViewById(R.id.backicon);


        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //mo ra form nhap thong tin
        iconeditDeliInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpen){
                    //neu dang dong thi mo ra
                    Animation animation = AnimationUtils.loadAnimation(CheckoutActivity.this, R.anim.iconrotate);
                    iconeditDeliInfor.startAnimation(animation);
                    inputDeliInfor.setVisibility(View.VISIBLE);
                    isOpen = true;
                } else {
                    Animation animation = AnimationUtils.loadAnimation(CheckoutActivity.this, R.anim.iconrotateback);
                    iconeditDeliInfor.startAnimation(animation);
                    inputDeliInfor.setVisibility(View.GONE);
                    isOpen = false;
                }
            }
        });

        //
        SharedPreferences sharedPreferences = CheckoutActivity.this.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int user_id = sharedPreferences.getInt("user_id", -1);


        Retrofit retrofit = RetrofitManager.getInstance();
        myapi = retrofit.create(myAPI.class);

        //lay ra du lieu ng dung
        Call<usermodel> call = myapi.getUserbyUserID(user_id);
        call.enqueue(new Callback<usermodel>() {
            @Override
            public void onResponse(Call<usermodel> call, Response<usermodel> response) {
                if(response.isSuccessful()){
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
                Toast.makeText(CheckoutActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //lay ra gia tien qua intent
        totalprice = findViewById(R.id.totalprice);
        totalbookprice= findViewById(R.id.totalbookprice);

        // recycler view lay ra don hang
        Call<ArrayList<cartDto>> callcartlist = myapi.getCartbyUserID(user_id);
        callcartlist.enqueue(new Callback<ArrayList<cartDto>>() {
            @Override
            public void onResponse(Call<ArrayList<cartDto>> call, Response<ArrayList<cartDto>> response) {
                if (response.isSuccessful()){
                    cartarrlist = response.body();
                    for(int i = 0; i< cartarrlist.size(); i ++){
                        totalQuantity += cartarrlist.get(i).getQuantity();
                        subtotal = cartarrlist.get(i).getQuantity()*cartarrlist.get(i).getBook_price();
                        totalbprice += subtotal;
                        Log.d("totalbprice", String.valueOf(totalbprice));
                        //call Adapter class
                        myadapter = new myCheckoutAdapter(cartarrlist, CheckoutActivity.this);
                        LinearLayoutManager layout = new LinearLayoutManager(CheckoutActivity.this, RecyclerView.VERTICAL, false);
                        rccvcheckoutitem.setLayoutManager(layout);
                        rccvcheckoutitem.setAdapter(myadapter);
                    }
                    totalbookprice.setText(String.format("$ %.2f", totalbprice));
                    totalprice.setText(String.format("$ %.2f", totalbprice + 5));
                } else {
                    Toast.makeText(CheckoutActivity.this, "NO RESPONSE", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<cartDto>> call, Throwable throwable) {
                Toast.makeText(CheckoutActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //xu ly khi dat hang
        placeOrdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cname = name.getText().toString().trim();
                String cemail = email.getText().toString().trim();
                String cphone = phone.getText().toString().trim();
                String cadd = address.getText().toString().trim();
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

                if (cadd.isEmpty()) {
                    address.setError("Please enter your address");
                    address.requestFocus();
                    return;
                }

                //call api lưu don hàng vào db
                Call<apiresponse> call = myapi.addtoOrder(user_id, totalQuantity, totalbprice + 5, cemail, cname,cphone, cadd );
                call.enqueue(new Callback<apiresponse>() {
                    @Override
                    public void onResponse(Call<apiresponse> call, Response<apiresponse> response) {
                        if(response.isSuccessful()){
                            apiresponse apiresponse = response.body();

                            AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);
                            builder.setMessage(apiresponse.getMessage());
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    Intent intenttoHP = new Intent(CheckoutActivity.this, MainActivity.class);
                                    startActivity(intenttoHP);
                                    finish();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();


                        } else {
                            Toast.makeText(CheckoutActivity.this, "NO RESPONSE", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<apiresponse> call, Throwable throwable) {
                        Toast.makeText(CheckoutActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
}