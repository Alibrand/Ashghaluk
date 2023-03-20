package com.ksacp2022.ashghaluk;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class TechnicianHomeActivity extends AppCompatActivity {


     AppCompatButton button_edit_profile,button_inbox,button_reviews,button_requests;
     FirebaseFirestore firebaseFirestore;
     FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_home);
        button_edit_profile = findViewById(R.id.button_edit_profile);
        button_inbox = findViewById(R.id.button_inbox);
        button_reviews = findViewById(R.id.button_reviews);
        button_requests = findViewById(R.id.button_requests);


        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        check_new_messages();


        button_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TechnicianHomeActivity.this,TechnicianEditProfileActivity. class);
                startActivity(intent);
            }
        });
        button_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TechnicianHomeActivity.this,TechnicianMyReviewsActivity. class);
                startActivity(intent);
            }
        });

        button_inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TechnicianHomeActivity.this,InboxActivity. class);
                startActivity(intent);
            }
        });

        button_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TechnicianHomeActivity.this,TechnicianServiceRequestsActivity. class);
                startActivity(intent);
            }
        });




    }

    private  void  check_new_messages(){
        button_inbox.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
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
                                                        button_inbox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_mark_email_unread_24,0,0,0);
                                                        makeText(TechnicianHomeActivity.this,"You have new messages..Check your Inbox" , LENGTH_LONG).show();

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
        check_new_messages();
    }
}