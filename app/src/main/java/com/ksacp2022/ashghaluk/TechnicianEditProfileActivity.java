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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TechnicianEditProfileActivity extends AppCompatActivity {

    AppCompatButton save;
    EditText service_description,facebook,twitter,price;
    Spinner service_category,region;
    String[] categories= {"Cleaning","Decoration","Maintenance","Landscaping","Restoration","Renovation"};
    String[] regions={"Abha","Al-Dammam","Riyadh", "Jeddah","Mekka", "Al Taef","Bisha","Hael","Najran"};
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_edit_profile);
        save = findViewById(R.id.save);
        service_description = findViewById(R.id.service_description);
        facebook = findViewById(R.id.facebook);
        twitter = findViewById(R.id.twitter);
        region = findViewById(R.id.region);
        service_category = findViewById(R.id.service_category);
        ArrayAdapter adapter=new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,categories);
        service_category.setAdapter(adapter);
        ArrayAdapter adapter1=new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,regions);
        region.setAdapter(adapter1);
        price = findViewById(R.id.price);


        progressDialog=new ProgressDialog(this);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        //loading profile info from firestore
        progressDialog.setMessage("Loading info");
        progressDialog.show();
        String uid=firebaseAuth.getUid();
        firebaseFirestore.collection("accounts")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful())
                        {

                            Map<String,Object> info=task.getResult().getData();
                            int selected_category= Arrays.asList(categories).indexOf(info.get("service_category").toString());
                            int selected_region= Arrays.asList(regions).indexOf(info.get("region").toString());
                            service_category.setSelection(selected_category);
                            region.setSelection(selected_region);
                            service_description.setText(info.get("service_description").toString());
                            facebook.setText(info.get("facebook_account").toString());
                            twitter.setText(info.get("twitter_account").toString());
                            price.setText(info.get("price").toString());

                        }
                        else{
                            Toast.makeText(TechnicianEditProfileActivity.this,"Failed to get info" , Toast.LENGTH_LONG).show();
                        finish();
                        }

                    }
                });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_service_description =service_description.getText().toString();
                String str_facebook =facebook.getText().toString();
                String str_twitter =twitter.getText().toString();
                String str_service_category=service_category.getSelectedItem().toString();
                String str_region =region.getSelectedItem().toString();
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
                newdata.put("region",str_region);
                newdata.put("service_description",str_service_description);
                newdata.put("twitter_account",str_twitter);
                newdata.put("facebook_account",str_facebook);
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
                                    Toast.makeText(TechnicianEditProfileActivity.this,"Changes Saved successfully" , Toast.LENGTH_LONG).show();


                                }
                                else
                                {
                                    Toast.makeText(TechnicianEditProfileActivity.this,"Failed to save changes" , Toast.LENGTH_LONG).show();
                                }

                            }
                        });
            }
        });






        }
}