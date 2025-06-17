package com.example.techh;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<News> newsList;
    private Context context;
    private String username; // Pass this from MainActivity or wherever you init the adapter
    private String category; // e.g. AI, IOT - also pass from activity if needed
    private BookmarkDatabaseHelper dbHelper;
    private Set<String> bookmarkedArticles = new HashSet<>();

    public NewsAdapter(Context context, List<News> newsList, String username, String category) {
        this.context = context;
        this.newsList = newsList;
        this.username = username;
        this.category = category;
        dbHelper = new BookmarkDatabaseHelper(context);

        // ✅ Correct this line:
        List<Bookmark> bookmarks = dbHelper.getAllBookmarks(username);
        for (Bookmark bookmark : bookmarks) {
            bookmarkedArticles.add(bookmark.getArticleUrl());
        }
    }





    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        if (newsList != null && position < newsList.size()) {
            News newsItem = newsList.get(position);
            holder.titleTextView.setText(newsItem.getTitle() != null ? newsItem.getTitle() : "No Title");
            holder.descriptionTextView.setText(newsItem.getDescription() != null ? newsItem.getDescription() : "No Description");

            if (newsItem.getImageUrl() != null && !newsItem.getImageUrl().isEmpty()) {
                Glide.with(context)
                        .load(newsItem.getImageUrl())
                        .placeholder(R.drawable.placeholder_image)
                        .into(holder.newsImageView);
            } else {
                holder.newsImageView.setImageResource(R.drawable.placeholder_image);
            }

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra("url", newsItem.getArticleUrl());
                context.startActivity(intent);
            });

            // Set bookmark icon based on DB state
            if (bookmarkedArticles.contains(newsItem.getArticleUrl())) {
                holder.bookmarkIcon.setImageResource(R.drawable.ic_bookmarked);
            } else {
                holder.bookmarkIcon.setImageResource(R.drawable.ic_bookmark);
            }

            // Toggle bookmark and update DB
            holder.bookmarkIcon.setOnClickListener(v -> {
                if (bookmarkedArticles.contains(newsItem.getArticleUrl())) {
                    bookmarkedArticles.remove(newsItem.getArticleUrl());
                    dbHelper.deleteBookmark(username, newsItem.getArticleUrl());
                    holder.bookmarkIcon.setImageResource(R.drawable.ic_bookmark);
                    Toast.makeText(context, "Bookmark removed", Toast.LENGTH_SHORT).show();
                } else {
                    bookmarkedArticles.add(newsItem.getArticleUrl());

                    String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                    Bookmark bookmark = new Bookmark(
                            username,
                            newsItem.getTitle(),
                            newsItem.getArticleUrl(),
                            currentDate,
                            category
                    );
                    dbHelper.addBookmark(bookmark);
                    holder.bookmarkIcon.setImageResource(R.drawable.ic_bookmarked);
                    Toast.makeText(context, "Bookmarked!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (newsList != null) ? newsList.size() : 0;
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView;
        ImageView newsImageView, bookmarkIcon;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.news_title);
            descriptionTextView = itemView.findViewById(R.id.news_description);
            newsImageView = itemView.findViewById(R.id.news_image);
            bookmarkIcon = itemView.findViewById(R.id.bookmark);
        }
    }

    public void updateNewsList(List<News> updatedList) {
        this.newsList = updatedList;
        notifyDataSetChanged();
    }
}