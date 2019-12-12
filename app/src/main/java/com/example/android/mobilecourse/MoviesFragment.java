package com.example.android.mobilecourse;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesFragment extends Fragment implements CustomAdapter.OnItemListener{

    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_TITTLE = "title";
    public static final String EXTRA_DESCRIPTION = "description";
    public static final String EXTRA_YEAR = "year";
    public static final String ANDROID_NET_CONN_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout linearLayout;
    private View movieView;

    public MoviesFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        movieView = inflater.inflate(R.layout.fragment_movies, container, false);

        initViews();
        loadMovies();
        registerNetworkMonitoring();

        return movieView;
    }

    private void initViews() {
        recyclerView = movieView.findViewById(R.id.welcome_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        linearLayout = movieView.findViewById(R.id.movie_fr_linearLayout);
        swipeRefreshLayout = movieView.findViewById(R.id.welcome_swipe_refresh);
        setupSwipeToRefresh();
    }

    private void loadMovies() {
        swipeRefreshLayout.setRefreshing(true);
        final MovieApi apiService = getApplicationEx().getMovieService();
        final Call<List<Movie>> call = apiService.getAllMovies();
        final CustomAdapter.OnItemListener fragment = this;

        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(final Call<List<Movie>> call,
                                   final Response<List<Movie>> response) {
                OnResponseFilmBody(response, fragment);
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t){
                Snackbar.make(linearLayout, R.string.failure, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void OnResponseFilmBody(Response<List<Movie>> response, CustomAdapter.OnItemListener fragment) {
        adapter = new CustomAdapter(response.body());
        recyclerView.setAdapter(adapter);
        CustomAdapter.setOnItemListener(fragment);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void setupSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    loadMovies();
                    swipeRefreshLayout.setRefreshing(false);
                }
        );
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    }

    private void registerNetworkMonitoring() {
        IntentFilter filter = new IntentFilter(ANDROID_NET_CONN_CONNECTIVITY_CHANGE);
        NetworkChangeReceiver receiver = new NetworkChangeReceiver(linearLayout);
        getActivity().registerReceiver(receiver, filter);
    }

    private App getApplicationEx() {
        return ((App) Objects.requireNonNull(getActivity()).getApplication());
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        Movie clickedItem = adapter.getMovieList().get(position);

        intent.putExtra(EXTRA_URL, clickedItem.getPoster());
        intent.putExtra(EXTRA_TITTLE, clickedItem.getTitle());
        intent.putExtra(EXTRA_YEAR, clickedItem.getYear());
        intent.putExtra(EXTRA_DESCRIPTION, clickedItem.getDescription());

        startActivity(intent);
    }

}
