package com.example.techh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private List<News> newsList;
    private String currentCategory = "technology";  // Default category
    private String currentQuery = "";

    private EditText searchBar;
    private ImageButton searchButton, userbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        newsList = new ArrayList<>();
        String username = "ayushridalvii";
        String category = "tech";
        newsAdapter = new NewsAdapter(this, newsList, username, category);

        recyclerView.setAdapter(newsAdapter);

        searchBar = findViewById(R.id.searchBar);
        searchButton = findViewById(R.id.search);
        userbutton = findViewById(R.id.userbtn);


        searchButton.setOnClickListener(v -> {
            String query = searchBar.getText().toString().trim();
            if (!query.isEmpty()) {
                currentQuery = query; // Save current search query
                fetchNewsByQuery(query);
            } else {
                Toast.makeText(MainActivity.this, "Please enter a search term", Toast.LENGTH_SHORT).show();
            }
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_cybersecurity) {
                currentCategory = "cybersecurity";
            } else if (id == R.id.nav_ai) {
                currentCategory = "artificial intelligence";
            } else if (id == R.id.nav_cloud) {
                currentCategory = "cloud computing";
            } else if (id == R.id.nav_blockchain) {
                currentCategory = "blockchain";
            } else if (id == R.id.nav_machine) {
                currentCategory = "machine learning";
            } else if (id == R.id.nav_iot) {
                currentCategory = "IOT";
            }

            currentQuery = "";
            fetchNewsByCategory(currentCategory);
            drawerLayout.closeDrawers();
            return true;
        });

        fetchNewsByCategory(currentCategory);
    }

    private void fetchNewsByCategory(String category) {
        NewsFetcher newsFetcher = new NewsFetcher(this);
        newsFetcher.fetchNews(category, new NewsFetcher.NewsCallback() {
            @Override
            public void onSuccess(List<News> fetchedNewsList) {
                runOnUiThread(() -> {
                    newsList.clear();
                    newsList.addAll(fetchedNewsList);
                    newsAdapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Failed to load news: " + errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void fetchNewsByQuery(String query) {
        NewsFetcher newsFetcher = new NewsFetcher(this);
        // ✅ The correct method call:
        newsFetcher.fetchNewsByQuery(query, new NewsFetcher.NewsCallback() {
            @Override
            public void onSuccess(List<News> fetchedNewsList) {
                runOnUiThread(() -> {
                    newsList.clear();
                    newsList.addAll(fetchedNewsList);
                    newsAdapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Search failed: " + errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else if (!currentQuery.isEmpty()) {
            // If search was active, go back to default category
            currentQuery = "";
            searchBar.setText("");
            fetchNewsByCategory(currentCategory);
        } else {
            super.onBackPressed();
        }
    }
     public void click(View v)
     {
         Intent intent = new Intent(MainActivity.this, UserActivity.class);
         startActivity(intent);
     }
    public void addBookmark(String articleTitle) {
        SharedPreferences prefs = getSharedPreferences("bookmarks", MODE_PRIVATE);
        Set<String> bookmarkSet = prefs.getStringSet("bookmarkSet", new HashSet<>());
        bookmarkSet.add(articleTitle);
        prefs.edit().putStringSet("bookmarkSet", bookmarkSet).apply();
    }

}
