package com.ksacp2022.ashghaluk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TechnicianReviewsAdapter extends RecyclerView.Adapter<TechnicianReviewCard> {

    List<Review> reviewList;
    Context context;

    public TechnicianReviewsAdapter(List<Review> reviewList, Context context) {
        this.reviewList = reviewList;
        this.context = context;
    }

    @NonNull
    @Override
    public TechnicianReviewCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.technician_review_card,parent,false);

        return new TechnicianReviewCard(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TechnicianReviewCard holder, int position) {
        Review review=reviewList.get(position);
        holder.text_view_name.setText(review.getName());
        holder.text_view_review.setText(review.getReview());
        float rating=Float.parseFloat(review.getRating());
        holder.rating.setRating(rating);

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}
class TechnicianReviewCard extends RecyclerView.ViewHolder{

    TextView text_view_name,text_view_review;
    RatingBar rating;


    public TechnicianReviewCard(@NonNull View itemView) {
        super(itemView);
        text_view_name=itemView.findViewById(R.id.text_view_name);
        text_view_review=itemView.findViewById(R.id.text_view_review);
        rating=itemView.findViewById(R.id.rating);
    }
}
