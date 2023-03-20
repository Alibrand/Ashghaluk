package com.ksacp2022.ashghaluk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;

public class TechnicianRequestsListAdapter extends RecyclerView.Adapter<TechnicianRequestCard> {

    List<ServiceRequest> requestList;
    Context context;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;

    public TechnicianRequestsListAdapter(List<ServiceRequest> requestList, Context context) {
        this.requestList = requestList;
        this.context = context;
        firestore=FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @NonNull
    @Override
    public TechnicianRequestCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.technician_request_card,parent,false);

        return new TechnicianRequestCard(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TechnicianRequestCard holder, int position) {
        ServiceRequest request= requestList.get(position);
        holder.text_view_name.setText(request.getClient_name());
        holder.text_view_price.setText(request.getPrice());
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
        holder.text_view_date.setText(sdf.format(request.getRequest_date()));

        if(request.getStatus().equals("Done"))
        {
            holder.image_pending.setVisibility(View.GONE);
            holder.image_done.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.image_pending.setVisibility(View.VISIBLE);
            holder.image_done.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }
}

class TechnicianRequestCard extends RecyclerView.ViewHolder{

    TextView text_view_name,text_view_date,text_view_price;
    ImageView image_done,image_pending;



    public TechnicianRequestCard(@NonNull View itemView) {
        super(itemView);
        text_view_name=itemView.findViewById(R.id.text_view_name);
        text_view_date=itemView.findViewById(R.id.text_view_date);
        text_view_price=itemView.findViewById(R.id.text_view_price);
        image_pending=itemView.findViewById(R.id.image_pending);
        image_done=itemView.findViewById(R.id.image_done);
    }
}
