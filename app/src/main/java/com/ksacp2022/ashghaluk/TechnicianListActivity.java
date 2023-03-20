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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TechnicianListActivity extends AppCompatActivity {
    TextView title;
    RecyclerView recycler_view_accounts;

    List<Account> accountList;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_list);

        title = findViewById(R.id.title);
        recycler_view_accounts = findViewById(R.id.recycler_view_accounts);
        firestore=FirebaseFirestore.getInstance();

        firebaseAuth=FirebaseAuth.getInstance();

        String service_category=getIntent().getStringExtra("category");

        title.setText(service_category+ " Technicians");


        dialog=new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Loading Accounts");

        dialog.show();
        firestore.collection("accounts")
                .whereEqualTo("type","Technician")
                .whereEqualTo("status","Active")
                .whereEqualTo("service_category",service_category)
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
                                //skip current user profile
                                if(account.getId().equals(firebaseAuth.getUid()))
                                    continue;
                                accountList.add(account);

                            }
                            TechAccountAdapter adapter=new TechAccountAdapter(accountList,TechnicianListActivity.this);
                            recycler_view_accounts.setAdapter(adapter);
                            dialog.dismiss();

                        }
                        else{
                            Toast.makeText(TechnicianListActivity.this,"Failed to get accounts" , Toast.LENGTH_LONG).show();
                            finish();

                        }

                    }
                });
    }
}