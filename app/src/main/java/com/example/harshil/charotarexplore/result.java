package com.example.harshil.charotarexplore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class result extends AppCompatActivity {
    cached cached = new cached();
    data data = new data();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar loading;
    public ArrayList<resultData> list;
    public RecyclerView result_view;
    public resultAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private String[] favorites_array;
    public static String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getIntent().getStringExtra("from") != null && getIntent().getStringExtra("from").equals("home")) {
            from = "home";
            getSupportActionBar().setTitle("Favourites");
        } else {
            from = "category";
            getSupportActionBar().setTitle("Search result");
        }

        loading = (ProgressBar) findViewById(R.id.loading);
        result_view = (RecyclerView) findViewById(R.id.result_view);
        list = new ArrayList<>();
        gridLayoutManager = new GridLayoutManager(this, 1);
        result_view.setLayoutManager(gridLayoutManager);
        adapter = new resultAdapter(result.this, list);
        result_view.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                favoritesapi();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        favoritesapi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toHome:
                Intent intent = new Intent(result.this, home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (result.from.equals("home"))
            startActivity(new Intent(result.this, home.class));
        else if (result.from.equals("category"))
            startActivity(new Intent(result.this, category.class));
    }

    private void favoritesapi() {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.link) + "getFav";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    if (result.getString("status").equals("1")) {
                        JSONArray favorites = result.getJSONArray("favorites");
                        favorites_array = new String[favorites.length()];
                        for (int i = 0; i < favorites.length(); i++) {
                            JSONObject favorite = favorites.getJSONObject(i);
                            favorites_array[i] = favorite.getString("result_id");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                resultapi();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                loading.setVisibility(View.GONE);
                Toast.makeText(result.this, "Something is wrong.", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("uid", cached.getUser_id(getApplicationContext()));
                return MyData;
            }
        };
        requestQueue.add(MyStringRequest);
    }

    private void resultapi() {
        list.clear();
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url;
        if (from.equals("category"))
            url = getResources().getString(R.string.link) + "result";
        else
            url = getResources().getString(R.string.link) + "getAllresults";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    if (result.getString("status").equals("1")) {
                        JSONArray results = result.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject jsonObject = results.getJSONObject(i);
                            if (from.equals("category")) {
                                if (favorites_array != null && favorites_array.length > 0) {
                                    if (!Arrays.asList(favorites_array).contains(jsonObject.getString("id"))) {
                                        resultData resultData = new resultData(jsonObject.getString("id"), jsonObject.getString("name"), jsonObject.getString("number"), jsonObject.getString("address"), jsonObject.getString("time"), jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("image"), jsonObject.getString("category_id"));
                                        list.add(resultData);
                                    }
                                } else {
                                    resultData resultData = new resultData(jsonObject.getString("id"), jsonObject.getString("name"), jsonObject.getString("number"), jsonObject.getString("address"), jsonObject.getString("time"), jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("image"), jsonObject.getString("category_id"));
                                    list.add(resultData);
                                }
                            } else {
                                if (favorites_array != null && favorites_array.length > 0) {
                                    if (Arrays.asList(favorites_array).contains(jsonObject.getString("id"))) {
                                        resultData resultData = new resultData(jsonObject.getString("id"), jsonObject.getString("name"), jsonObject.getString("number"), jsonObject.getString("address"), jsonObject.getString("time"), jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("image"), jsonObject.getString("category_id"));
                                        list.add(resultData);
                                    }
                                }
                            }
                        }
                    }
                    if (list.size() > 0) {
                        if (from.equals("category")) {
                            Toast.makeText(result.this, list.size() + " results found", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(result.this, list.size() + " favourites found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (from.equals("category")) {
                            Toast.makeText(result.this, "No results found.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(result.this, "You have no favourites.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    loading.setVisibility(View.GONE);
                    result_view.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                loading.setVisibility(View.GONE);
                Toast.makeText(result.this, "Something is wrong.", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("vid", data.getVid());
                MyData.put("cid", data.getCid());
                if (from.equals("category"))
                    return MyData;
                return null;
            }
        };
        requestQueue.add(MyStringRequest);
    }
}
