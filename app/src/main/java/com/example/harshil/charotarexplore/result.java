package com.example.harshil.charotarexplore;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import java.util.HashMap;
import java.util.Map;

public class result extends AppCompatActivity {
    data data = new data();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar loading;
    public ArrayList<resultData> list;
    public RecyclerView result_view;
    public resultAdapter adapter;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loading = (ProgressBar) findViewById(R.id.loading);
        result_view = (RecyclerView) findViewById(R.id.result_view);
        list = new ArrayList<>();
        resultapi();
        gridLayoutManager = new GridLayoutManager(this, 1);
        result_view.setLayoutManager(gridLayoutManager);
        adapter = new resultAdapter(result.this, list);
        result_view.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resultapi();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void resultapi() {
        list.clear();
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.link) + "result";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    if (result.getString("status").equals("1")) {
                        JSONArray results = result.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject jsonObject = results.getJSONObject(i);
                            resultData resultData = new resultData(jsonObject.getString("id"), jsonObject.getString("name"), jsonObject.getString("number"), jsonObject.getString("address"), jsonObject.getString("time"), jsonObject.getString("latitude"), jsonObject.getString("longitude"), getResources().getString(R.string.image_link) + jsonObject.getString("image"));
                            list.add(resultData);
                        }
                    } else {
                        Toast.makeText(result.this, result.getString("message"), Toast.LENGTH_SHORT).show();
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
                return MyData;
            }
        };
        requestQueue.add(MyStringRequest);
    }
}
