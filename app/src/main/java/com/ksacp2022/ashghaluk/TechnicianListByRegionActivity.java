package com.ksacp2022.ashghaluk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TechnicianListByRegionActivity extends AppCompatActivity {


    RecyclerView recycler_view_accounts;
AppCompatImageButton refresh;
    List<Account> accountList;
    AppCompatSpinner region;
    FirebaseFirestore firestore;
    ProgressDialog dialog;
    String[] regions={"Abha","Al-Dammam","Riyadh", "Jeddah","Mekka", "Al Taef","Bisha","Hael","Najran"};
    String selected_region="Abha";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_list_by_region);
        recycler_view_accounts = findViewById(R.id.recycler_view_accounts);
        region = findViewById(R.id.region);
        ArrayAdapter adapter=new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,regions);
        region.setAdapter(adapter);
        refresh = findViewById(R.id.refresh);


        firestore=FirebaseFirestore.getInstance();
        dialog=new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


        load_accounts();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_region=region.getSelectedItem().toString();
                load_accounts();
            }
        });


    }

    private void load_accounts() {
        dialog.setMessage("Loading Accounts");

        dialog.show();
        firestore.collection("accounts")
                .whereEqualTo("type","Technician")
                .whereEqualTo("status","Active")
                .whereEqualTo("region",selected_region)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            accountList=new ArrayList<Account>();

                            for (DocumentSnapshot doc:task.getResult().getDocuments()
                            ) {
                                Map<String,Object> data=doc.getData();
                                Account account=new Account();
                                account.setName(data.get("name").toString());
                                account.setService_description(data.get("service_description").toString());
                                account.setRegion(data.get("region").toString());
                                account.setId(doc.getId());
                                account.setRating(data.get("rating").toString());

                                accountList.add(account);

                            }
                            TechAccountAdapter adapter=new TechAccountAdapter(accountList,TechnicianListByRegionActivity.this);
                            recycler_view_accounts.setAdapter(adapter);
                            dialog.dismiss();

                        }
                        else{
                            Toast.makeText(TechnicianListByRegionActivity.this,"Failed to get accounts" , Toast.LENGTH_LONG).show();
                            finish();

                        }

                    }
                });
    }
}