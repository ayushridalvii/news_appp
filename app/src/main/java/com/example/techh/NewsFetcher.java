package com.example.techh;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsFetcher {

    private static final String API_KEY = "0d030b684f2842aba25660a77a97d71c";
    private static final String BASE_URL = "https://newsapi.org/v2/everything?apiKey=" + API_KEY + "&q=";
    private final OkHttpClient client;

    public interface NewsCallback {
        void onSuccess(List<News> newsList);
        void onFailure(String errorMessage);
    }

    public NewsFetcher(Context context) {
        this.client = new OkHttpClient();
    }

    // Map categories to keywords for better search accuracy
    private String mapCategoryToKeyword(String category) {
        switch (category.toLowerCase()) {
            case "iot":
                return "IOT";
            case "machine learning":
                return "machine learning";
            case "artificial intelligence":
                return "AI";
            case "cybersecurity":
                return "cyber security";
            case "cloud computing":
                return "cloud technology";
            case "blockchain":
                return "blockchain technology";
            case "technology":
                return "latest technology";
            default:
                return category;
        }
    }

    // Fetch news by category
    public void fetchNews(String category, final NewsCallback callback) {
        String keyword = mapCategoryToKeyword(category);
        String url = BASE_URL + keyword + "&sortBy=publishedAt&language=en&pageSize=30";
        Log.d("NewsFetcher", "Fetching category-based URL: " + url);
        makeApiRequest(url, callback);
    }

    // Fetch news by direct search query
    public void fetchNewsByQuery(String query, final NewsCallback callback) {
        String url = BASE_URL + query + "&sortBy=publishedAt&language=en&pageSize=30";
        Log.d("NewsFetcher", "Fetching search-based URL: " + url);
        makeApiRequest(url, callback);
    }

    // Common method to handle API requests and parsing
    private void makeApiRequest(String url, final NewsCallback callback) {
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("NewsFetcher", "API request failed: " + e.getMessage());
                callback.onFailure("API request failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onFailure("Unexpected response code: " + response.code());
                    return;
                }

                String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONArray articles = jsonObject.getJSONArray("articles");

                    List<News> newsList = new ArrayList<>();
                    for (int i = 0; i < articles.length(); i++) {
                        JSONObject article = articles.getJSONObject(i);
                        String title = article.optString("title");
                        String description = article.optString("description");
                        String imageUrl = article.optString("urlToImage");
                        String url = article.optString("url");

                        if (!title.isEmpty() && !description.isEmpty()) {
                            newsList.add(new News(title, description, imageUrl, url));
                        }
                    }

                    callback.onSuccess(newsList);
                } catch (JSONException e) {
                    callback.onFailure("JSON parsing failed: " + e.getMessage());
                }
            }
        });
    }
}
