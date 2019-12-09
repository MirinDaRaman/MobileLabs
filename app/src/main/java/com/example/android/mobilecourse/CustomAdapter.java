package com.example.android.mobilecourse;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private List<Movie> movieList;
    private static OnItemListener mOnItemListener;

    public interface OnItemListener {
        void onItemClick(int position);
    }

    CustomAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    List<Movie> getMovieList() {
        return movieList;
    }

    public static void setOnItemListener(OnItemListener listener) {
        CustomAdapter.mOnItemListener = listener;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_view, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        holder.textDesctiption.setText(movieList.get(position).getDescription());
        Picasso.get().load(movieList.get(position).getPoster()).into(holder.poster);
        holder.movieYear.setText(String.format("Year: %s", Integer.toString(movieList.get(position).getYear())));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
