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

public class SocialWorkerGreenPointsActivity extends AppCompatActivity {
    BottomNavigationView bnView;
    UserModel currentUser;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_worker_green_points);
        txt = (TextView) findViewById(R.id.txt_green_points);
        bnView = (BottomNavigationView) findViewById(R.id.bnView_sw);
        getUserGreenPoints();
        Menu menu = bnView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bnView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.nav_home_sw)
                {
                    startActivity(new Intent(SocialWorkerGreenPointsActivity.this, SocialWorkerHomeActivity.class));
                    finish();
                    return true;
                }
                else if(id == R.id.nav_profile_sw)
                {
                    startActivity(new Intent(SocialWorkerGreenPointsActivity.this, SocialWorkerProfileActivity.class));
                    finish();
                    return true;
                }
                else if(id == R.id.nav_green_points_sw)
                {
                    //startActivity(new Intent(GreenPointsActivity.this, GreenPointsActivity.class));
                }
                else if(id == R.id.nav_report_info_sw)
                {
                    startActivity(new Intent(SocialWorkerGreenPointsActivity.this, SocialWorkerStatusActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    private void getUserGreenPoints() {
        FireBaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            currentUser = task.getResult().toObject(UserModel.class);
            String greenPoints_txt = "You currently have "+currentUser.getGreenPoints()+" Green Points!";
            txt.setText(greenPoints_txt);
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SocialWorkerGreenPointsActivity.this, SocialWorkerHomeActivity.class));
        super.onBackPressed();
    }
}