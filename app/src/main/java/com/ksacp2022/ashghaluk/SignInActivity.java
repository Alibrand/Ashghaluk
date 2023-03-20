package com.ksacp2022.ashghaluk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class SignInActivity extends AppCompatActivity {

    EditText email,password;
    TextView forgot_password,signup;
    AppCompatButton log_in;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        forgot_password = findViewById(R.id.forgot_password);
        signup = findViewById(R.id.signup);
        log_in = findViewById(R.id.log_in);
        progressDialog=new ProgressDialog(SignInActivity.this);

        firebaseAuth=FirebaseAuth.getInstance();



        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this,ForgotPasswordActivity. class);
                startActivity(intent);
            }
        });



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                  String stremail =email.getText().toString();
                  String strpassword =password.getText().toString();

                  if(stremail.equals(""))
                  {
                      email.setError("Empty field");
                      return;
                  }
                if(strpassword.equals(""))
                {
                    password.setError("Empty field");
                    return;
                }
                progressDialog.setMessage("Please Wait");
                progressDialog.setCancelable(false);
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(stremail,strpassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    if(stremail.equals("admin@ashghaluk.com"))
                                    {
                                        Intent intent = new Intent(SignInActivity.this,AdminHomeActivity. class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {

                                    Intent intent = new Intent(SignInActivity.this,HomeActivity. class);
                                    startActivity(intent);
                                    finish();
                                    }
                                }
                                else{
                                    Toast.makeText(SignInActivity.this,"Error :"+task.getException().getMessage().toLowerCase() , Toast.LENGTH_LONG).show();
                                }
                            }
                        });




            }
        });

    }
}