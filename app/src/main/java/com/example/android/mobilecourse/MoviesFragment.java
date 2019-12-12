package com.example.android.mobilecourse;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;
import java.util.Objects;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
    private CoordinatorLayout linearLayout;
    private View movieView;
    private FloatingActionButton floatingActionButton;
    private DatabaseReference databaseFilms;

    public MoviesFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        movieView = inflater.inflate(R.layout.fragment_movies, container, false);

        initViews();
        databaseFilms = FirebaseDatabase.getInstance().getReference();

        databaseFilms.child("Films").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                swipeRefreshLayout.setRefreshing(true);
                List<Movie> movieList = adapter.getMovieList();
                if ( movieList.size() > 0 ) {
                    movieList.clear();
                }
                for ( DataSnapshot snapshot : dataSnapshot.getChildren() ) {
                    Movie movie = new Movie(
                            snapshot.child("title").getValue(String.class),
                            snapshot.child("year").getValue(Long.class),
                            snapshot.child("rating").getValue(Long.class),
                            snapshot.child("description").getValue(String.class),
                            snapshot.child("poster").getValue(String.class)
                    );
                    movieList.add(movie);
                    adapter.setMovieList(movieList);
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        registerNetworkMonitoring();
        return movieView;
    }

    private void initViews() {
        recyclerView = movieView.findViewById(R.id.welcome_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        linearLayout = movieView.findViewById(R.id.movie_fr_linearLayout);
        swipeRefreshLayout = movieView.findViewById(R.id.welcome_swipe_refresh);
        floatingActionButton = movieView.findViewById(R.id.fab);
        setupSwipeToRefresh();

        adapter = new CustomAdapter(null);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemListener(this);

        floatingActionButton.setOnClickListener(view -> ((MainActivity) Objects.requireNonNull(getActivity())).getViewPager().setCurrentItem(1));
    }

    private void setupSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
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
