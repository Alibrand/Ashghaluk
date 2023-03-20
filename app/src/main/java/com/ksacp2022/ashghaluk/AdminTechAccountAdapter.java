package com.ksacp2022.ashghaluk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminTechAccountAdapter extends RecyclerView.Adapter<AdminTechAccountCard> {
    List<Account> accounts;
    Context context;

    public AdminTechAccountAdapter(List<Account> accounts, Context context) {
        this.accounts = accounts;
        this.context=context;
    }

    @NonNull
    @Override
    public AdminTechAccountCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_card,parent,false);
        return new AdminTechAccountCard(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminTechAccountCard holder, int position) {
        Account account=accounts.get(position);
        holder.text_view_name.setText(account.getName());
        holder.text_view_category.setText(account.getService_category());
        holder.text_view_status.setText(account.getStatus());
        if(account.getStatus().equals("Active"))
        {
            holder.text_view_status.setTextColor(Color.GREEN);
        }
        else if(account.getStatus().equals("Rejected"))
        {
            holder.text_view_status.setTextColor(Color.RED);
        }
        else{
            holder.text_view_status.setTextColor(Color.MAGENTA);
        }

        holder.account_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,AdminTechnicianAccountActivity.class);
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

class AdminTechAccountCard extends RecyclerView.ViewHolder {

    TextView text_view_name,text_view_category,text_view_status;
    CardView account_card;

    public AdminTechAccountCard(@NonNull View itemView) {
        super(itemView);
        text_view_name=itemView.findViewById(R.id.text_view_name);
        text_view_category=itemView.findViewById(R.id.text_view_category);
        text_view_status=itemView.findViewById(R.id.text_view_status);
        account_card=itemView.findViewById(R.id.account_card);
    }
}
