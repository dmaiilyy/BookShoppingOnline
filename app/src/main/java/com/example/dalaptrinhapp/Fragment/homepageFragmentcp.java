package com.example.dalaptrinhapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dalaptrinhapp.API.myAPI;
import com.example.dalaptrinhapp.Activity.BookListActivity;
import com.example.dalaptrinhapp.Activity.LoginActivity;
import com.example.dalaptrinhapp.Adapter.myBookHPAdapter;
import com.example.dalaptrinhapp.Model.bookmodel;
import com.example.dalaptrinhapp.R;
import com.example.dalaptrinhapp.RetrofitManager.RetrofitManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class homepageFragmentcp extends Fragment {
    private TextView viewallbook, intentlogin;
    private RecyclerView homepagerccv;
    private ArrayList<bookmodel> bookmodelarraylist;
    private myAPI mybookapi;
    private myBookHPAdapter myhpadapter;
    private LinearLayout loginlayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_homepage, container, false);
        loginlayout = rootView.findViewById(R.id.loginlayout);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int user_id = sharedPreferences.getInt("user_id", -1);

        if(user_id != -1){
            // da dang nhap
            loginlayout.setVisibility(View.GONE);
            Log.d("hpuser_id", String.valueOf(user_id));
        } else{
            //chua dang nhap
            loginlayout.setVisibility(View.VISIBLE);
            intentlogin = rootView.findViewById(R.id.intentlogin);
            intentlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intenttologin = new Intent(getContext(), LoginActivity.class);
                    startActivity(intenttologin);
                }
            });
        }

        viewallbook = rootView.findViewById(R.id.viewallbook);
        viewallbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_to_booklist = new Intent(getContext(), BookListActivity.class);
                startActivity(intent_to_booklist);
            }
        });

        //lay ra list new books
        homepagerccv = rootView.findViewById(R.id.homepagerccv);

        bookmodelarraylist = new ArrayList<>();
        Retrofit retrofit = RetrofitManager.getInstance();
        mybookapi = retrofit.create(myAPI.class);

        Call<ArrayList<bookmodel>> newBooklist = mybookapi.callNewBooklist();
        newBooklist.enqueue(new Callback<ArrayList<bookmodel>>() {
            @Override
            public void onResponse(Call<ArrayList<bookmodel>> call, Response<ArrayList<bookmodel>> response) {
                bookmodelarraylist = response.body();
                for (int i =0; i < bookmodelarraylist.size(); i ++){
                    myhpadapter = new myBookHPAdapter(bookmodelarraylist, getContext());
                    LinearLayoutManager layout = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
                    homepagerccv.setLayoutManager(layout);
                    homepagerccv.setAdapter(myhpadapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<bookmodel>> call, Throwable throwable) {
                Toast.makeText(getContext(), "retrofit Error", Toast.LENGTH_SHORT);
            }
        });

        return rootView;
    }

}
