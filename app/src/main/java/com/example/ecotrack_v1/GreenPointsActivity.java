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

public class GreenPointsActivity extends AppCompatActivity {
    BottomNavigationView bnView;
    FloatingActionButton fab;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_points);
        txt = (TextView) findViewById(R.id.txt_green_points);
        bnView = (BottomNavigationView) findViewById(R.id.bnView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        txt.setText("Your Green Points will appear here!");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GreenPointsActivity.this, ReportActivity.class));
                finish();
            }
        });
        Menu menu = bnView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bnView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.nav_home)
                {
                    startActivity(new Intent(GreenPointsActivity.this, HomeActivity.class));
                    finish();
                    return true;
                }
                else if(id == R.id.nav_profile)
                {
                    startActivity(new Intent(GreenPointsActivity.this, ProfileActivity.class));
                    finish();
                    return true;
                }
                else if(id == R.id.nav_green_points)
                {
                    //startActivity(new Intent(GreenPointsActivity.this, GreenPointsActivity.class));
                }
                else if(id == R.id.nav_report_info)
                {
                    startActivity(new Intent(GreenPointsActivity.this, StatusActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GreenPointsActivity.this, HomeActivity.class));
        super.onBackPressed();
    }
}