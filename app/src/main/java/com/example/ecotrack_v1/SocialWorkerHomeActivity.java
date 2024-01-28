package com.example.ecotrack_v1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SocialWorkerHomeActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Article> articleList =  new ArrayList<>();
    NewsRecyclerAdapter adapter;
    LinearProgressIndicator progressIndicator;
    TextView txt, welcome;
    BottomNavigationView bnView;
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_worker_home);
        bnView = (BottomNavigationView) findViewById(R.id.sw_bnView);
        progressIndicator = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.news_recycler_view);
        welcome = (TextView) findViewById(R.id.txt_news_headline);
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        setWelcomeText();

        Menu menu = bnView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bnView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.nav_home_sw)
                {
                    //startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                }
                else if(id == R.id.nav_profile_sw)
                {
                    startActivity(new Intent(SocialWorkerHomeActivity.this, SocialWorkerProfileActivity.class));
                    return true;
                }
                else if(id == R.id.nav_green_points_sw)
                {
                    startActivity(new Intent(SocialWorkerHomeActivity.this, SocialWorkerGreenPointsActivity.class));
                    return true;
                }
                else if(id == R.id.nav_report_info_sw)
                {
                    startActivity(new Intent(SocialWorkerHomeActivity.this, SocialWorkerStatusActivity.class));
                    return true;
                }
                return false;
            }
        });
        setUpRecyclerView();
        getNews();

    }

    private void setWelcomeText() {
        fStore = FirebaseFirestore.getInstance();

        String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value!=null && value.exists()) {
                    String start = "Hello ";
                    String name = value.getString("fullname");
                    String end = " ! \nWe appreciate your commitment towards a clean society. You might find these interesting";
                    String final_txt = start+name+end;
                    welcome.setText(final_txt);
                }
            }
        });
    }

    public void changeInProgress(boolean show)
    {
        if(show)
            progressIndicator.setVisibility(View.VISIBLE);
        else
            progressIndicator.setVisibility(View.INVISIBLE);
    }
    public void setUpRecyclerView()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsRecyclerAdapter(articleList);
        recyclerView.setAdapter(adapter);
    }
    public void getNews()
    {
        adapter.getContext(SocialWorkerHomeActivity.this);
        changeInProgress(true);
        NewsApiClient newsApiClient = new NewsApiClient("193fa1c0348b416ea3ce859d78d1d699");
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("Cleanliness and Environment")
                        .language("en")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        runOnUiThread(()->{
                            changeInProgress(false);
                            articleList = response.getArticles();
                            adapter.updateData(articleList);
                            adapter.notifyDataSetChanged();

                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i("GOT FAILURE", throwable.toString());

                    }
                }
        );
    }

    @Override
    protected void onRestart() {
        setUpRecyclerView();
        getNews();
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}