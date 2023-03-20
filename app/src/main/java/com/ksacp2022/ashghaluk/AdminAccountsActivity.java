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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminAccountsActivity extends AppCompatActivity {
    TextView title;
    RecyclerView recycler_view_accounts;

    List<Account> accountList;

    FirebaseFirestore firestore;
    ProgressDialog dialog;

    String accounts_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_accounts);
        title = findViewById(R.id.title);
        recycler_view_accounts = findViewById(R.id.recycler_view_accounts);
        firestore=FirebaseFirestore.getInstance();

        accounts_status=getIntent().getStringExtra("status");

        title.setText(accounts_status+" Accounts");


        dialog=new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Loading Accounts");

        load_accounts();










    }

    private void load_accounts() {
        dialog.show();
        firestore.collection("accounts")
                .whereEqualTo("type","Technician")
                .whereEqualTo("status",accounts_status)
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
                                account.setService_category(data.get("service_category").toString());
                                account.setStatus(data.get("status").toString());
                                account.setId(doc.getId());

                                accountList.add(account);

                            }
                            AdminTechAccountAdapter adapter=new AdminTechAccountAdapter(accountList,AdminAccountsActivity.this);
                            recycler_view_accounts.setAdapter(adapter);
                            dialog.dismiss();

                        }
                        else{
                            Toast.makeText(AdminAccountsActivity.this,"Failed to get accounts" , Toast.LENGTH_LONG).show();
                            finish();

                        }

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        load_accounts();
    }
}