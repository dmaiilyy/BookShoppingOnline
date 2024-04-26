package com.example.dalaptrinhapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dalaptrinhapp.Adapter.myFragmentAdapter;
import com.example.dalaptrinhapp.Fragment.homepageFragment;
import com.example.dalaptrinhapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivitycp extends AppCompatActivity {

    ViewPager2 viewpager;
    BottomNavigationView btnav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //3 fragment home
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

        viewpager = findViewById(R.id.viewpager);
        btnav = findViewById(R.id.btnavigation);

        fragmentArrayList.add(new homepageFragment());
//        fragmentArrayList.add(new cartFragment());
//        fragmentArrayList.add(new personalFragment());

        myFragmentAdapter myadpt = new myFragmentAdapter(this, fragmentArrayList);
        viewpager.setAdapter(myadpt);

        // slide fragments
        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        btnav.setSelectedItemId(R.id.homepage);
                        break;
                    case 1:
                        btnav.setSelectedItemId(R.id.cart);
                        break;
                    case 2:
                        btnav.setSelectedItemId(R.id.user);
                        break;
                    default:
                        btnav.setSelectedItemId(R.id.homepage);
                        break;
                }
                super.onPageSelected(position);
            }
        });

        btnav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.homepage) {
                    viewpager.setCurrentItem(0);
                    return true;
                }else if(item.getItemId() == R.id.cart) {
                    Intent intent_to_cart = new Intent(MainActivitycp.this, CartActivity.class);
                    startActivity(intent_to_cart);
                    return item.getItemId() == R.id.homepage;
                }else if (item.getItemId() == R.id.booklist) {
                    Intent intent_to_booklist = new Intent(MainActivitycp.this, BookListActivity.class);
                    startActivity(intent_to_booklist);
                    return item.getItemId() == R.id.homepage;
                }else if (item.getItemId() == R.id.user) {
                    Intent intent_to_usersetting = new Intent(MainActivitycp.this, UserSettingActivity.class);
                    startActivity(intent_to_usersetting);
                    return item.getItemId() == R.id.homepage;
                }
                return true;
            }
        });


    }
}
