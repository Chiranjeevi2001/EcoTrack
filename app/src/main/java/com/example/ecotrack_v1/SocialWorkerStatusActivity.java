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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SocialWorkerStatusActivity extends AppCompatActivity {
    BottomNavigationView bnView;

    ArrayList<ReportModel> reportModelArrayList;
    RecyclerView reportRecyclerView;
    SocialWorkerReportRecyclerAdapter reportRecyclerAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(SocialWorkerStatusActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching your reports");
        progressDialog.show();
        setContentView(R.layout.activity_social_worker_status);
        bnView = (BottomNavigationView) findViewById(R.id.bnView_sw);
        reportRecyclerView = findViewById(R.id.reports_recycler_view_sw);
        reportRecyclerView.setHasFixedSize(true);
        reportRecyclerView.setLayoutManager(new LinearLayoutManager(SocialWorkerStatusActivity.this));

        db = FirebaseFirestore.getInstance();

        reportModelArrayList = new ArrayList<ReportModel>();

        reportRecyclerAdapter = new SocialWorkerReportRecyclerAdapter(SocialWorkerStatusActivity.this, reportModelArrayList);

        reportRecyclerView.setAdapter(reportRecyclerAdapter);
        reportRecyclerAdapter.setOnItemClickListener(position -> {
            // Handle item removal
            removeItem(position);
        });
        EventChangeListener();
        Menu menu = bnView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bnView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.nav_home_sw)
                {
                    startActivity(new Intent(SocialWorkerStatusActivity.this, SocialWorkerHomeActivity.class));
                    finish();
                    return true;
                }
                else if(id == R.id.nav_profile_sw)
                {
                    startActivity(new Intent(SocialWorkerStatusActivity.this, SocialWorkerProfileActivity.class));
                    finish();
                }
                else if(id == R.id.nav_green_points_sw)
                {
                    startActivity(new Intent(SocialWorkerStatusActivity.this, SocialWorkerGreenPointsActivity.class));
                    finish();
                    return true;
                }
                else if(id == R.id.nav_report_info_sw)
                {
                    //startActivity(new Intent(StatusActivity.this, StatusActivity.class));
                    //return true;
                }
                return false;
            }
        });

    }
    private void removeItem(int position) {
        reportModelArrayList.remove(position);
        reportRecyclerView.getAdapter().notifyItemRemoved(position);
    }

    private void EventChangeListener() {
        db.collection("reports")
                .orderBy("isCleaned", Query.Direction.ASCENDING)
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
        startActivity(new Intent(SocialWorkerStatusActivity.this, SocialWorkerHomeActivity.class));
        super.onBackPressed();
    }
}