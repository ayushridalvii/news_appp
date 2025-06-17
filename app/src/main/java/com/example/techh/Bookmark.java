package com.example.techh;

public class Bookmark {
    private String username;
    private String articleUrl;
    private String title;
    private String date;
    private String category;

    public Bookmark(String username, String articleUrl, String title, String date, String category) {
        this.username = username;
        this.articleUrl = articleUrl;
        this.title = title;
        this.date = date;
        this.category = category;
    }

    public String getUsername() { return username; }
    public String getArticleUrl() { return articleUrl; }
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getCategory() { return category; }
}
