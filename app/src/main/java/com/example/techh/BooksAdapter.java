package com.example.techh;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {

    private List<Bookmark> bookmarks;

    public BooksAdapter(List<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public void setBookmarks(List<Bookmark> newBookmarks) {
        this.bookmarks = newBookmarks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bookmark_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Bookmark bookmark = bookmarks.get(position);
        holder.titleTextView.setText(bookmark.getTitle());
        holder.categoryTextView.setText("Category: " + bookmark.getCategory());
        holder.dateTextView.setText("Date: " + bookmark.getDate());
    }

    @Override
    public int getItemCount() {
        return (bookmarks != null) ? bookmarks.size() : 0;
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, categoryTextView, dateTextView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}
