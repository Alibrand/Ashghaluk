package com.ksacp2022.ashghaluk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TechnicianFormActivity extends AppCompatActivity {

    AppCompatButton apply;
    EditText service_description,facebook,twitter,price;
    Spinner service_category;
    String[] categories= {"Cleaning","Decoration","Maintenance","Landscaping","Restoration","Renovation"};
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_form);
        apply = findViewById(R.id.apply);
        service_description = findViewById(R.id.service_description);
        facebook = findViewById(R.id.facebook);
        twitter = findViewById(R.id.twitter);
        service_category = findViewById(R.id.service_category);
        ArrayAdapter adapter=new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,categories);
        service_category.setAdapter(adapter);
        progressDialog=new ProgressDialog(this);
        price = findViewById(R.id.price);


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str_service_description =service_description.getText().toString();
                String str_facebook =facebook.getText().toString();
                String str_twitter =twitter.getText().toString();
                String str_service_category=service_category.getSelectedItem().toString();
                String str_price =price.getText().toString();

                if(str_price.equals(""))
                {
                    price.setError("Please set a price");
                    return;
                }

                if(str_service_description.equals(""))
                {
                    service_description.setError("Please fill a short service description");
                    return;
                }
                progressDialog.setMessage("Please Wait");
                progressDialog.show();

                String uid=firebaseAuth.getUid();

                //prepare data
                Map<String,Object> newdata=new HashMap<String,Object>();
                newdata.put("service_category",str_service_category);
                newdata.put("service_description",str_service_description);
                newdata.put("twitter_account",str_twitter);
                newdata.put("facebook_account",str_facebook);
                newdata.put("type","Technician");
                newdata.put("status","Pending");
                newdata.put("price",str_price);

                firebaseFirestore.collection("accounts")
                        .document(uid)
                        .update(newdata)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful())
                                {
                                    Intent intent = new Intent(TechnicianFormActivity.this,ApplySuccessActivity. class);
                                    startActivity(intent);
                                    finish();

                                }
                                else
                                {
                                    Toast.makeText(TechnicianFormActivity.this,"Failed to apply" , Toast.LENGTH_LONG).show();
                                }

                            }
                        });
            }
        });









    }
}