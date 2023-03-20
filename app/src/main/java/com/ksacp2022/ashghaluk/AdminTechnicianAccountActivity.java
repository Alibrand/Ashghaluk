package com.ksacp2022.ashghaluk;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminTechnicianAccountActivity extends AppCompatActivity {

    TextView name, category, service_description, region, status;
    AppCompatButton button_call, button_active, button_reject;

    FirebaseFirestore firestore;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_technician_account);
        name = findViewById(R.id.name);
        category = findViewById(R.id.category);
        service_description = findViewById(R.id.service_description);
        region = findViewById(R.id.region);
        status = findViewById(R.id.status);
        button_call = findViewById(R.id.button_call);
        button_active = findViewById(R.id.button_active);
        button_reject = findViewById(R.id.button_reject);

        String tech_id = getIntent().getStringExtra("tech_id");

        firestore = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Loading Account");
        dialog.show();


        firestore.collection("accounts")
                .document(tech_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            DocumentSnapshot doc = task.getResult();
                            Map<String, Object> data = doc.getData();
                            name.setText(data.get("name").toString());
                            category.setText(data.get("service_category").toString());
                            service_description.setText(data.get("service_description").toString());
                            region.setText(data.get("region").toString());
                            status.setText(data.get("status").toString());

                            button_call.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + data.get("phone").toString()));
                                    startActivity(intent);
                                }
                            });


                        } else {
                            makeText(AdminTechnicianAccountActivity.this, "Failed to load account", LENGTH_LONG).show();
                            finish();
                        }
                    }

                });

        button_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setMessage("Activating Account");
                dialog.show();
                Map<String,Object> new_data=new HashMap<>();
                new_data.put("status","Active");
                firestore.collection("accounts")
                        .document(tech_id)
                        .update(new_data)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    makeText(AdminTechnicianAccountActivity.this,"Account Activated Successfully" , LENGTH_LONG).show();
                                    finish();
                                }
                                else{
                                    makeText(AdminTechnicianAccountActivity.this,"Failed to activate account" , LENGTH_LONG).show();

                                }
                            }
                        });
            }
        });

        button_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setMessage("Rejecting Account");
                dialog.show();
                Map<String,Object> new_data=new HashMap<>();
                new_data.put("status","Rejected");
                firestore.collection("accounts")
                        .document(tech_id)
                        .update(new_data)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    makeText(AdminTechnicianAccountActivity.this,"Account Rejected Successfully" , LENGTH_LONG).show();
                                    finish();
                                }
                                else{
                                    makeText(AdminTechnicianAccountActivity.this,"Failed to reject account" , LENGTH_LONG).show();

                                }
                            }
                        });
            }
        });


    }
}