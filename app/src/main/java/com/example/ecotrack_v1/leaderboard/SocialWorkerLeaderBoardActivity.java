package com.example.ecotrack_v1.leaderboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ecotrack_v1.GreenPointsActivity;
import com.example.ecotrack_v1.R;
import com.example.ecotrack_v1.SocialWorkerGreenPointsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SocialWorkerLeaderBoardActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<ScoreData> list;
    ScoreAdapter adapter;
    Button back;

    FirebaseFirestore firestore ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        back = findViewById(R.id.leaderboard_back_button);
        recyclerView = findViewById(R.id.LeaderRecyclerView);
        progressBar = findViewById(R.id.LeaderprogressBar);
        firestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = (CollectionReference) firestore.collection("users");

        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        collectionReference
                .orderBy("greenPoints", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            //ScoreData data = documentSnapshot.toObject(ScoreData.class);
                            String fullname = documentSnapshot.getString("fullname");
                            long greenPoints = documentSnapshot.getLong("greenPoints");
                            ScoreData data1 = new ScoreData();
                            data1.setFullname(fullname);
                            data1.setGreenPoints(greenPoints);
                            list.add(data1);

                        }

                        adapter = new ScoreAdapter(list, SocialWorkerLeaderBoardActivity.this);
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SocialWorkerLeaderBoardActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SocialWorkerLeaderBoardActivity.this, SocialWorkerGreenPointsActivity.class));
                finish();
            }
        });

    }
}