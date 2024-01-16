package com.example.ecotrack_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StatusActivity extends AppCompatActivity {
    BottomNavigationView bnView;
    FloatingActionButton fab;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        txt = (TextView) findViewById(R.id.txt_status);
        bnView = (BottomNavigationView) findViewById(R.id.bnView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        txt.setText("This is Status Activity");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StatusActivity.this, ReportActivity.class));
                finish();
            }
        });
        Menu menu = bnView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
        bnView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.nav_home)
                {
                    startActivity(new Intent(StatusActivity.this, HomeActivity.class));
                    finish();
                    return true;
                }
                else if(id == R.id.nav_profile)
                {
                   startActivity(new Intent(StatusActivity.this, ProfileActivity.class));
                    finish();
                }
                else if(id == R.id.nav_green_points)
                {
                    startActivity(new Intent(StatusActivity.this, GreenPointsActivity.class));
                    finish();
                    return true;
                }
                else if(id == R.id.nav_report_info)
                {
                    //startActivity(new Intent(StatusActivity.this, StatusActivity.class));
                    //return true;
                }
                return false;
            }
        });

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(StatusActivity.this, HomeActivity.class));
        super.onBackPressed();
    }
}