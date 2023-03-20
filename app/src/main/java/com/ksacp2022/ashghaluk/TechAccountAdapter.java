package com.ksacp2022.ashghaluk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TechAccountAdapter extends RecyclerView.Adapter<TechAccountCard> {
    List<Account> accounts;
    Context context;

    public TechAccountAdapter(List<Account> accounts, Context context) {
        this.accounts = accounts;
        this.context = context;
    }

    @NonNull
    @Override
    public TechAccountCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.technician_account_card,parent,false);
        return new TechAccountCard(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TechAccountCard holder, int position) {
        Account account=accounts.get(position);
        holder.text_view_name.setText(account.getName());
        holder.text_view_description.setText(account.getService_description());
        holder.text_view_region.setText(account.getRegion());
        float rate=Float.parseFloat(account.getRating());
        holder.rating.setRating(rate);


        holder.account_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,TechnicianAccountActivity.class);
                intent.putExtra("tech_id",account.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }
}


class TechAccountCard extends RecyclerView.ViewHolder{


    TextView text_view_name,text_view_description,text_view_region;
    CardView account_card;
    RatingBar rating;

    public TechAccountCard(@NonNull View itemView) {
        super(itemView);
        text_view_name=itemView.findViewById(R.id.text_view_name);
        text_view_description=itemView.findViewById(R.id.text_view_description);
        text_view_region=itemView.findViewById(R.id.text_view_region);
        account_card=itemView.findViewById(R.id.account_card);
        rating=itemView.findViewById(R.id.rating);
    }
}
