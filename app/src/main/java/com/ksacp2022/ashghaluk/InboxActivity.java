package com.ksacp2022.ashghaluk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InboxActivity extends AppCompatActivity {

    RecyclerView recycler_chats;

    FirebaseFirestore firestore;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    List<Chat> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        recycler_chats = findViewById(R.id.recycler_chats);

        firebaseAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        String uid=firebaseAuth.getUid();


        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading Chats");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        chatList=new ArrayList<>();
        firestore.collection("inbox")
                .whereArrayContains("users_ids",uid)
                .orderBy("last_update", Query.Direction.DESCENDING)
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
                              Chat chat=new Chat();
                              chat.setId(doc.getId());
                              chat.setUsers_names((List<String>) data.get("users_names"));
                              chat.setUsers_ids((List<String>) data.get("users_ids"));
                               chatList.add(chat);
                          }
                          ChatsListAdapter adapter=new ChatsListAdapter(chatList,InboxActivity.this);
                          recycler_chats.setAdapter(adapter);

                        }
                        else{
                            Toast.makeText(InboxActivity.this,"Failed to load chats" , Toast.LENGTH_LONG).show();
                        }
                    }
                });



    }
}