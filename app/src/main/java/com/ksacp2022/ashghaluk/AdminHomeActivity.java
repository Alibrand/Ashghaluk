package com.ksacp2022.ashghaluk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHomeActivity extends AppCompatActivity {

    AppCompatButton button_pending_accounts,button_active_accounts,button_rejected_accounts,
    button_logout;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        button_pending_accounts = findViewById(R.id.button_pending_accounts);
        button_active_accounts = findViewById(R.id.button_active_accounts);
        button_rejected_accounts = findViewById(R.id.button_rejected_accounts);
        button_logout = findViewById(R.id.button_logout);

        firebaseAuth=FirebaseAuth.getInstance();


        button_pending_accounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this,AdminAccountsActivity. class);
                intent.putExtra("status","Pending");
                startActivity(intent);
            }
        });
        button_active_accounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this,AdminAccountsActivity. class);
                intent.putExtra("status","Active");
                startActivity(intent);
            }
        });
        button_rejected_accounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this,AdminAccountsActivity. class);
                intent.putExtra("status","Rejected");
                startActivity(intent);
            }
        });

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intent = new Intent(AdminHomeActivity.this,SignInActivity. class);
                startActivity(intent);
                finish();
            }
        });





    }
}