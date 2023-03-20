package com.ksacp2022.ashghaluk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

public class MyRequestsListAdapter extends RecyclerView.Adapter<MyRequestCard> {

    List<ServiceRequest> requestList;
    Context context;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;

    public MyRequestsListAdapter(List<ServiceRequest> requestList, Context context) {
        this.requestList = requestList;
        this.context = context;
        firestore=FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @NonNull
    @Override
    public MyRequestCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_request_card,parent,false);

        return new MyRequestCard(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRequestCard holder, int position) {
        ServiceRequest request= requestList.get(position);
        holder.text_view_name.setText(request.getTechnician_name());
        holder.text_view_price.setText(request.getPrice());
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
        holder.text_view_date.setText(sdf.format(request.getRequest_date()));

        if(request.getStatus().equals("Done"))
        {
            holder.button_done.setVisibility(View.GONE);
            holder.image_done.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.button_done.setVisibility(View.VISIBLE);
            holder.image_done.setVisibility(View.GONE);
        }


        holder.button_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setMessage("Click yes if the service is done \nP.S:you can not undo this step")
                        .setTitle("Confirmation")
                        .setNegativeButton("Cancel",null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog.setMessage("Updating Request");
                                progressDialog.show();
                               firestore.collection("service_requests")
                                       .document(request.getId())
                                       .update("status","Done")
                                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               progressDialog.dismiss();
                                               if(task.isComplete())
                                               {
                                                   firestore.collection("accounts")
                                                           .document(request.getTechnician_id())
                                                           .update("completed_requests", FieldValue.increment(1));
                                                   request.setStatus("Done");
                                                   MyRequestsListAdapter.this.notifyDataSetChanged();
                                               }
                                               else
                                               {
                                                   Toast.makeText(context,"Failed to complete step" , Toast.LENGTH_LONG).show();
                                               }
                                           }
                                       });
                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }
}

class MyRequestCard extends RecyclerView.ViewHolder{

    TextView text_view_name,text_view_date,text_view_price;
    ImageView image_done;
    AppCompatButton button_done;


    public MyRequestCard(@NonNull View itemView) {
        super(itemView);
        text_view_name=itemView.findViewById(R.id.text_view_name);
        text_view_date=itemView.findViewById(R.id.text_view_date);
        text_view_price=itemView.findViewById(R.id.text_view_price);
        button_done=itemView.findViewById(R.id.button_done);
        image_done=itemView.findViewById(R.id.image_done);
    }
}
