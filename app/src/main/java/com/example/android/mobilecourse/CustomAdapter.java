package com.example.android.mobilecourse;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder>{

    private List<Movie> movieList;
    private OnItemListener mOnItemListener;

    public interface OnItemListener {
        void onItemClick(int position);
    }

    CustomAdapter(List<Movie> movieList) {
        this.movieList = new ArrayList<>();
        if ( movieList != null ) {
            this.movieList = movieList;
        }
    }

    List<Movie> getMovieList() {
        return movieList;
    }

    public void setOnItemListener(OnItemListener listener) {
        this.mOnItemListener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_view, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        holder.textDesctiption.setText(movieList.get(position).getDescription());
        Picasso.get().load(movieList.get(position).getPoster()).into(holder.poster);
        holder.movieYear.setText(String.format("Year: %s", Long.toString(movieList.get(position).getYear())));
        holder.rating.setText(String.format("My rating: %s",Long.toString(movieList.get(position).getRating())));
        holder.textTitle.setText(movieList.get(position).getTitle());
        holder.layout.setOnClickListener(view -> mOnItemListener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void setMovieList(List<Movie> movieList){
        this.movieList = movieList;
        notifyDataSetChanged();
    }
}
