package com.ksacp2022.ashghaluk;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    AppCompatButton button_logout,button_are_you_a_technician;
    AppCompatButton button_cleaning,button_decoration,button_maintenance,
    button_landscaping,button_restoration,button_renovation,button_my_service_requests;
    AppCompatButton button_browse_by_regions;
    TextView welcome;
    ImageView profile,inbox_icon;
    Account user_account;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    LinearLayoutCompat inbox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        welcome = findViewById(R.id.welcome);
        button_logout = findViewById(R.id.button_logout);
        button_are_you_a_technician = findViewById(R.id.button_are_you_a_technician);
        button_cleaning = findViewById(R.id.button_cleaning);
        button_decoration = findViewById(R.id.button_decoration);
        button_maintenance = findViewById(R.id.button_maintenance);
        button_landscaping = findViewById(R.id.button_landscaping);
        button_restoration = findViewById(R.id.button_restoration);
        button_renovation = findViewById(R.id.button_renovation);
        inbox = findViewById(R.id.inbox);
        profile = findViewById(R.id.profile);
        inbox_icon = findViewById(R.id.inbox_icon);
        button_my_service_requests = findViewById(R.id.button_my_service_requests);


        button_browse_by_regions = findViewById(R.id.button_browse_by_regions);




        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading User Info");

        //get user account information
        get_account_information();


        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intent = new Intent(HomeActivity.this,SignInActivity. class);
                startActivity(intent);
                finish();
            }
        });

        button_are_you_a_technician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,TechnicianFormActivity. class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_account.getStatus().equals("Pending")) {
                    Intent intent = new Intent(HomeActivity.this, ApplySuccessActivity.class);
                    startActivity(intent);
                }
                else if(user_account.getStatus().equals("Rejected"))
                {
                    Intent intent = new Intent(HomeActivity.this, RejectedReportActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(HomeActivity.this, TechnicianHomeActivity.class);
                    startActivity(intent);
                }
            }
        });

        button_cleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TechnicianListActivity.class);
                intent.putExtra("category","Cleaning");
                startActivity(intent);
            }
        });
        button_decoration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TechnicianListActivity.class);
                intent.putExtra("category","Decoration");
                startActivity(intent);
            }
        });
        button_maintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TechnicianListActivity.class);
                intent.putExtra("category","Maintenance");
                startActivity(intent);
            }
        });
        button_landscaping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TechnicianListActivity.class);
                intent.putExtra("category","Landscaping");
                startActivity(intent);
            }
        });

        button_restoration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TechnicianListActivity.class);
                intent.putExtra("category","Restoration");
                startActivity(intent);
            }
        });
        button_renovation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TechnicianListActivity.class);
                intent.putExtra("category","Renovation");
                startActivity(intent);
            }
        });


        button_browse_by_regions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TechnicianListByRegionActivity.class);

                startActivity(intent);
            }
        });

        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, InboxActivity.class);

                startActivity(intent);
            }
        });

        button_my_service_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MyServiceRequestsActivity.class);
                startActivity(intent);
            }
        });








    }

    private void get_account_information() {
        progressDialog.show();
        user_account=new Account();

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
                            Map<String,Object> account_data=task.getResult().getData();
                            user_account.setName(account_data.get("name").toString());
                            user_account.setStatus(account_data.get("status").toString());
                            user_account.setType(account_data.get("type").toString());

                            //set welcome
                            welcome.setText("Welcome "+user_account.getName());
                            //if the user is a technician show profile icon
                            //and hide the buuton
                            if(user_account.getType().equals("Technician"))
                            {
                                button_are_you_a_technician.setVisibility(View.GONE);
                                profile.setVisibility(View.VISIBLE);
                                inbox.setVisibility(View.INVISIBLE);

                            }
                            else{
                                inbox.setVisibility(View.VISIBLE);
                                check_new_messages();
                            }




                        }
                        else{
                            makeText(HomeActivity.this,"Something went wrong ! we will sign out now...try again later" , LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        finish();
                        }

                    }
                });

    }

    private  void  check_new_messages(){
        inbox_icon.setImageResource(R.drawable.ic_baseline_markunread_24);
        firebaseFirestore.collection("inbox")
                .whereArrayContains("users_ids",firebaseAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for (DocumentSnapshot doc:task.getResult().getDocuments()) {
                                doc.getReference()
                                        .collection("messages")
                                        .whereEqualTo("to",firebaseAuth.getUid())
                                        .whereEqualTo("status","unseen")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful())
                                                {
                                                   int new_messages_count= task.getResult().getDocuments().size();
                                                   if(new_messages_count>0)
                                                   {
                                                       inbox_icon.setImageResource(R.drawable.ic_baseline_mark_email_unread_24);
                                                       makeText(HomeActivity.this,"You have new messages..Check your Inbox" , LENGTH_LONG).show();
                                                   }

                                                }

                                            }
                                        });
                            }
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_account_information();
    }
}