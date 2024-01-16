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

public class HomeActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Article> articleList =  new ArrayList<>();
    NewsRecyclerAdapter adapter;
    LinearProgressIndicator progressIndicator;
    TextView txt, welcome;
    BottomNavigationView bnView;
    FloatingActionButton fab;
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bnView = (BottomNavigationView) findViewById(R.id.bnView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        progressIndicator = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.news_recycler_view);
        welcome = (TextView) findViewById(R.id.txt_news_headline);
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        setWelcomeText();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ReportActivity.class));
            }
        });
        Menu menu = bnView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bnView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.nav_home)
                {
                    //startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                }
                else if(id == R.id.nav_profile)
                {
                    startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                    return true;
                }
                else if(id == R.id.nav_green_points)
                {
                    startActivity(new Intent(HomeActivity.this, GreenPointsActivity.class));
                    return true;
                }
                else if(id == R.id.nav_report_info)
                {
                    startActivity(new Intent(HomeActivity.this, StatusActivity.class));
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
                    String end = " ! \nHere are some news articles for you:";
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
        adapter.getContext(HomeActivity.this);
        changeInProgress(true);
        NewsApiClient newsApiClient = new NewsApiClient("193fa1c0348b416ea3ce859d78d1d699");
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("Environment and climate: INDIA")
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