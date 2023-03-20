package com.ksacp2022.ashghaluk;

import static android.widget.Toast.*;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class RequestServiceActivity extends AppCompatActivity {

    AppCompatButton pay;
    EditText card_number,cvv;
    String tech_name,tech_id,service_price;
    TextView price;
    ProgressDialog progressDialog;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_service);
        price = findViewById(R.id.price);
        pay = findViewById(R.id.pay);
        card_number = findViewById(R.id.card_number);
        cvv = findViewById(R.id.cvv);

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        firestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();





        tech_id=getIntent().getStringExtra("tech_id");
        tech_name=getIntent().getStringExtra("tech_name");
        service_price=getIntent().getStringExtra("price");
        price.setText(service_price);


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number=card_number.getText().toString();
                String cvvn=cvv.getText().toString();
                if(number.isEmpty())
                {
                    card_number.setError("Required Field");
                    return;
                }
                if(number.length()!=16)
                {

                    card_number.setError("Invalid Card Number Code");
                    return;
                }
                if(cvvn.isEmpty())
                {
                    cvv.setError("Required Field");
                    return;
                }
                if(cvvn.length()!=3)
                {

                    cvv.setError("Invalid CVV Code");
                    return;
                }
                new AlertDialog.Builder(RequestServiceActivity.this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog.setMessage("Request in progress");
                                progressDialog.show();

                                firestore.collection("accounts")
                                        .document(firebaseAuth.getUid())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful())
                                                {
                                                    DocumentSnapshot doc=task.getResult();
                                                    Map<String,Object> data=doc.getData();
                                                    String name=data.get("name").toString();
                                                    ServiceRequest request=new ServiceRequest();
                                                    request.setClient_id(firebaseAuth.getUid());
                                                    request.setClient_name(name);
                                                    request.setTechnician_id(tech_id);
                                                    request.setTechnician_name(tech_name);
                                                    request.setPrice(service_price);
                                                    firestore.collection("service_requests")
                                                            .add(request)
                                                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                                    if(task.isSuccessful())
                                                                    {
                                                                        firestore.collection("accounts")
                                                                                .document(tech_id)
                                                                                        .update("total_requests", FieldValue.increment(1));
                                                                        makeText(RequestServiceActivity.this,"Thanks for choosing our services" , LENGTH_LONG).show();
                                                                    progressDialog.dismiss();
                                                                    finish();
                                                                    }
                                                                    else{
                                                                        makeText(RequestServiceActivity.this,"Something wrong happened" , LENGTH_LONG).show();
                                                                    progressDialog.dismiss();
                                                                    }
                                                                }
                                                            });
                                                }
                                                else{
                                                    makeText(RequestServiceActivity.this,"Something wrong happened" , LENGTH_LONG).show();
                                                    progressDialog.dismiss();
                                                }

                                            }
                                        });



                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .show();
            }
        });

    }
}