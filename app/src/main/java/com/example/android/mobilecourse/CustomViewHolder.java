package com.example.android.mobilecourse;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

class CustomViewHolder extends RecyclerView.ViewHolder {

   public RelativeLayout layout;
   public TextView textTitle;
   public TextView textDesctiption;
   public ImageView poster;
   public TextView movieYear;
   public TextView rating;

   CustomViewHolder(final View itemView) {
       super(itemView);
       layout = (RelativeLayout) itemView;
       textDesctiption = itemView.findViewById(R.id.custom_desctription);
       poster = itemView.findViewById(R.id.custom_imageView);
       textTitle = itemView.findViewById(R.id.custom_title);
       movieYear = itemView.findViewById(R.id.custom_year);
       rating = itemView.findViewById(R.id.rating);
   }
}
