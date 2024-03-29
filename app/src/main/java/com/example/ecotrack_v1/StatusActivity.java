package com.example.ecotrack_v1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StatusActivity extends AppCompatActivity {
    BottomNavigationView bnView;

    ArrayList<ReportModel> reportModelArrayList;
    RecyclerView reportRecyclerView;
    ReportRecyclerAdapter reportRecyclerAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(StatusActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching your reports");
        progressDialog.show();
        setContentView(R.layout.activity_status);
        bnView = (BottomNavigationView) findViewById(R.id.bnView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        reportRecyclerView = findViewById(R.id.reports_recycler_view);
        reportRecyclerView.setHasFixedSize(true);
        reportRecyclerView.setLayoutManager(new LinearLayoutManager(StatusActivity.this));


        db = FirebaseFirestore.getInstance();

        reportModelArrayList = new ArrayList<ReportModel>();

        reportRecyclerAdapter = new ReportRecyclerAdapter(StatusActivity.this, reportModelArrayList);

        reportRecyclerView.setAdapter(reportRecyclerAdapter);
        EventChangeListener();
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

    private void EventChangeListener() {
        db.collection("reports")
                .whereEqualTo("reportedUser",FireBaseUtil.currentUserId())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null)
                        {
                            if(progressDialog.isShowing())
                            {
                                progressDialog.dismiss();
                            }
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        if(value.getDocumentChanges().size() == 0)
                        {
                            progressDialog.dismiss();
                        }
                        for(DocumentChange dc: value.getDocumentChanges())
                        {
                            if(dc.getType() == DocumentChange.Type.ADDED)
                            {
                                reportModelArrayList.add(dc.getDocument().toObject(ReportModel.class));

                            }

                            reportRecyclerAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();


                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(StatusActivity.this, HomeActivity.class));
        super.onBackPressed();
    }
}