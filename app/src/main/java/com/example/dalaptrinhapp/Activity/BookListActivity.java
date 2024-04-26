package com.example.dalaptrinhapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dalaptrinhapp.API.myAPI;
import com.example.dalaptrinhapp.Adapter.myBookAdapter;
import com.example.dalaptrinhapp.Model.bookmodel;
import com.example.dalaptrinhapp.Model.categorymodel;
import com.example.dalaptrinhapp.R;
import com.example.dalaptrinhapp.RetrofitManager.RetrofitManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookListActivity extends AppCompatActivity {
    //USING JSON
    private myAPI mybookapi,mycategoryapi;
    private ArrayList<bookmodel> bookmodelarraylist;
    private myBookAdapter myadapter;
    private RecyclerView recyclerView;
    private ImageView backicon,carticon;
    private SearchView searchview;
    private TextView emptynotify;
    private AutoCompleteTextView autoCompletetxt;
    ArrayList<categorymodel> categorylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        emptynotify=findViewById(R.id.emptynotify);
        recyclerView = findViewById(R.id.recyclerviewBooklist);
        bookmodelarraylist = new ArrayList<>();
        viewJsonData();

        //backicon
        backicon = findViewById(R.id.backicon);
        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        carticon = findViewById(R.id.carticon);
        carticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttoHP = new Intent(BookListActivity.this, CartActivity.class);
                startActivity(intenttoHP);
            }
        });

        //search view
        searchview = findViewById(R.id.searchview);
        searchview.clearFocus();
//        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filterList(newText);
//                return true;
//            }
//        });

        //category spinner
        autoCompletetxt = findViewById(R.id.autoCompletetxt);
        autoCompletetxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int selectedCategoryId = -1;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                for (categorymodel category : categorylist) {
                    if (category.getName().equalsIgnoreCase(selectedItem)) {
                        selectedCategoryId = category.getCategory_id();
                        break;
                    }
                }
                ArrayList<bookmodel> filterlist = new ArrayList<>();
                for (bookmodel item : bookmodelarraylist){
                    if (item.getCategory_id() == selectedCategoryId ){
                        filterlist.add(item);
                    }
                }
                if (filterlist.isEmpty()){
                    //k co gi
                    myadapter.setFilterlist(filterlist);
                    emptynotify.setText("No data found!");
                } else{
                    myadapter.setFilterlist(filterlist);
                    emptynotify.setText("");
                }
            }
        });

        //chạm vào ngoài thanh search view tự động đóng bàn phím ảo
        ViewGroup parentLayout = findViewById(R.id.display_booklist);
        parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Rect outRect = new Rect();
                    searchview.getGlobalVisibleRect(outRect);
                    if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchview.getWindowToken(), 0);
                        searchview.clearFocus();
                    }
                }
                return false;
            }
        });
    }

    private void viewJsonData() {
        Retrofit retrofit = RetrofitManager.getInstance();
        mybookapi = retrofit.create(myAPI.class);

        Call<ArrayList<bookmodel>> modelarraylist = mybookapi.callArraylist();
        modelarraylist.enqueue(new Callback<ArrayList<bookmodel>>() {
            @Override
            public void onResponse(Call<ArrayList<bookmodel>> call, Response<ArrayList<bookmodel>> response) {
                bookmodelarraylist = response.body();
                for(int i = 0; i< bookmodelarraylist.size(); i ++){
                    //call Adapter class
                    myadapter = new myBookAdapter(bookmodelarraylist, BookListActivity.this);
                    LinearLayoutManager layout = new LinearLayoutManager(BookListActivity.this,RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(layout);
                    recyclerView.setAdapter(myadapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<bookmodel>> call, Throwable throwable) {
                Toast.makeText(BookListActivity.this, "retrofit Error", Toast.LENGTH_SHORT);
            }
        });

        //lay ra Category
        mycategoryapi = retrofit.create(myAPI.class);
        Call<ArrayList<categorymodel>> callCate = mycategoryapi.callCategoryList();
        callCate.enqueue(new Callback<ArrayList<categorymodel>>() {
            @Override
            public void onResponse(Call<ArrayList<categorymodel>> call, Response<ArrayList<categorymodel>> response) {
                    categorylist = response.body();
                    ArrayList<String> categorynamelist = new ArrayList<>();
                    for (categorymodel item : categorylist) {
                        categorynamelist.add(item.getName());
                    }
                    //khi k mo dropdown
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(BookListActivity.this, R.layout.list_cate_item, categorynamelist);
                    autoCompletetxt.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<ArrayList<categorymodel>> call, Throwable throwable) {
            }
        });
    }
//    private void filterList(String text) {
//        ArrayList<bookmodel> filterlist = new ArrayList<>();
//        for (bookmodel item : bookmodelarraylist){
//            if (item.getTitle().toLowerCase().startsWith(text.toLowerCase()) ||
//                    item.getAuthor().toLowerCase().startsWith(text.toLowerCase())){
//                filterlist.add(item);
//            }
//        }
//        if (filterlist.isEmpty()){
//            myadapter.setFilterlist(filterlist);
//            emptynotify.setText("No data found!");
//        } else{
//            myadapter.setFilterlist(filterlist);
//        }
//    }

}