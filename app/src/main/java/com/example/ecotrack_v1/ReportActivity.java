package com.example.ecotrack_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class ReportActivity extends AppCompatActivity {

    BottomNavigationView bnView;
    FloatingActionButton fab;
    Button mapButton;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        txt = (TextView) findViewById(R.id.txt_report);
        bnView = (BottomNavigationView) findViewById(R.id.bnView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mapButton = (Button) findViewById(R.id.btn_map);
        txt.setText("This is Report Activity");

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReportActivity.this, MapsReportActivity.class));
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(ReportActivity.this, ReportActivity.class));
            }
        });
        Menu menu = bnView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bnView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.nav_home)
                {
                    startActivity(new Intent(ReportActivity.this, HomeActivity.class));
                    finish();
                    return true;
                }
                else if(id == R.id.nav_profile)
                {
                    startActivity(new Intent(ReportActivity.this, ProfileActivity.class));
                    finish();
                    return true;
                }
                else if(id == R.id.nav_green_points)
                {
                    startActivity(new Intent(ReportActivity.this, GreenPointsActivity.class));
                    finish();
                    return true;
                }
                else if(id == R.id.nav_report_info)
                {
                    startActivity(new Intent(ReportActivity.this, StatusActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

    }
}