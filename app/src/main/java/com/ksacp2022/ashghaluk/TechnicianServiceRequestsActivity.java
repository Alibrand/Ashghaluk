package com.ksacp2022.ashghaluk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TechnicianServiceRequestsActivity extends AppCompatActivity {
    RecyclerView recycler_requests;
    TextView title;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    List<ServiceRequest> requestList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_service_requests);
        recycler_requests = findViewById(R.id.recycler_requests);
        title = findViewById(R.id.title);


        firebaseAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        String uid=firebaseAuth.getUid();


        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading Requests");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        requestList =new ArrayList<>();
        firestore.collection("service_requests")
                .whereEqualTo("technician_id",uid)
                .orderBy("request_date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful())
                        {
                            for (DocumentSnapshot doc:task.getResult().getDocuments())
                            {
                                Map<String,Object> data=doc.getData();
                                ServiceRequest request=new ServiceRequest();
                                request.setClient_name(data.get("client_name").toString());
                                request.setTechnician_id(data.get("technician_id").toString());
                                request.setId(doc.getId());
                                request.setPrice(data.get("price").toString());
                                request.setStatus(data.get("status").toString());
                                Timestamp request_date= (Timestamp) data.get("request_date");
                                request.setRequest_date(request_date.toDate());
                                requestList.add(request);
                            }
                            title.setText("Service Requests ("+task.getResult().getDocuments().size()+")");
                            TechnicianRequestsListAdapter adapter=new TechnicianRequestsListAdapter(requestList,TechnicianServiceRequestsActivity.this);
                            recycler_requests.setAdapter(adapter);

                        }
                        else{
                            Toast.makeText(TechnicianServiceRequestsActivity.this,"Failed to load requests" , Toast.LENGTH_LONG).show();
                        }
                    }
                });



    }
}