package com.ksacp2022.ashghaluk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText email;
    AppCompatButton send;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.email);
        send = findViewById(R.id.send);
        progressDialog=new ProgressDialog(ForgotPasswordActivity.this);

        firebaseAuth=FirebaseAuth.getInstance();


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stremail =email.getText().toString();

                if(stremail.equals(""))
                {
                    email.setError("Empty Field");
                    return;
                }

                progressDialog.setMessage("Please Wait");
                progressDialog.setCancelable(false);
                progressDialog.show();
                firebaseAuth.sendPasswordResetEmail(stremail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful())
                                {
                                    Intent intent = new Intent(ForgotPasswordActivity.this,ResetSuccessActivity. class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(ForgotPasswordActivity.this,"Error :"+task.getException().getMessage().toString() , Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });

    }
}