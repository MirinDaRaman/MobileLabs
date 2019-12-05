package com.example.android.mobilecourse;

import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout linearLayout;
    public static final IntentFilter INTENT_FILTER = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        loadMovies();
        registerNetworkMonitoring();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.welcome_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        linearLayout = findViewById(R.id.linearLayout);
        swipeRefreshLayout = findViewById(R.id.welcome_swipe_refresh);
        setupSwipeToRefresh();
    }

    private void loadMovies() {
        swipeRefreshLayout.setRefreshing(true);
        final MovieApi apiService = getApplicationEx().getMovieService();
        final Call<List<Movie>> call = apiService.getAllMovies();

        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(final Call<List<Movie>> call,
                                   final Response<List<Movie>> response) {
                setAdapter(response);
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Snackbar.make(linearLayout, R.string.failed, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void setAdapter(Response<List<Movie>> response) {
        adapter = new CustomAdapter(response.body());
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void setupSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    loadMovies();
                    swipeRefreshLayout.setRefreshing(false);
                }
        );
        swipeRefreshLayout.setColorSchemeResources(R.color.primary_darker);
    }

    private void registerNetworkMonitoring() {
        NetworkChangeReceiver receiver = new NetworkChangeReceiver(linearLayout);
        this.registerReceiver(receiver, INTENT_FILTER);
    }

    private App getApplicationEx() {
        return ((App) getApplication());
    }
}