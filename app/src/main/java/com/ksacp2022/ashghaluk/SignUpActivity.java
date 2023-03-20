package com.ksacp2022.ashghaluk;

import static android.widget.Toast.*;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    EditText full_name, email, password,
            confirm_password, phone;
    TextView sign_in;
    AppCompatSpinner region;
    AppCompatButton sign_up;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        full_name = findViewById(R.id.full_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        phone = findViewById(R.id.phone);
        sign_in = findViewById(R.id.sign_in);
        region = findViewById(R.id.region);
        sign_up = findViewById(R.id.sign_up);
        String[] regions = {"Abha", "Al-Dammam", "Riyadh", "Jeddah", "Mekka", "Al Taef", "Bisha", "Hael", "Najran"};
        ArrayAdapter adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, regions);
        region.setAdapter(adapter);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strfull_name = full_name.getText().toString();
                String stremail = email.getText().toString();
                String strpassword = password.getText().toString();
                String strconfirm_password = confirm_password.getText().toString();
                String strphone = phone.getText().toString();
                String strregion = region.getSelectedItem().toString();

                if (strfull_name.equals("")) {
                    full_name.setError("Empty filed not allowed");
                    full_name.requestFocus();
                    return;
                } else if (stremail.equals("")) {
                    email.setError("Empty filed not allowed");
                    email.requestFocus();
                    return;
                } else if (check_email_validity(stremail) == false) {
                    email.setError("Email should be like user@example.com");
                    email.requestFocus();
                    return;
                } else if (strpassword.equals("")) {
                    password.setError("Password cannot be empty");
                    password.requestFocus();
                    return;
                } else if (strconfirm_password.equals("")) {
                    confirm_password.setError("This field is required");
                    confirm_password.requestFocus();
                    return;
                } else if (check_password_validity(strpassword) == false) {
                    password.setError("Password should be at least 6 characters contains  upper and lower case letters");
                    password.requestFocus();
                    return;
                } else if (!strpassword.equals(strconfirm_password)) {
                    confirm_password.setError("Passwords not match");
                    confirm_password.requestFocus();
                    return;
                } else if (strphone.equals("")) {
                    phone.setError("Empty filed not allowed");
                    phone.requestFocus();
                    return;
                }

                progressDialog.setMessage("Please Wait");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(stremail, strpassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String new_user_id = firebaseAuth.getUid();
                                    Account new_account = new Account();
                                    new_account.setName(strfull_name);
                                    new_account.setPhone(strphone);
                                    new_account.setRegion(strregion);
                                    firebaseFirestore.collection("accounts")
                                            .document(new_user_id)
                                            .set(new_account)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressDialog.dismiss();
                                                    if (task.isSuccessful()) {
                                                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                        finish();

                                                    } else {
                                                        makeText(SignUpActivity.this, "Error :" + task.getException().getMessage(), LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                } else {
                                    makeText(SignUpActivity.this, "Error :" + task.getException().getMessage(), LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });


            }


            private boolean check_email_validity(String email) {
                Pattern pattern;
                Matcher matcher;
                String epattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,6}$";
                pattern = Pattern.compile(epattern);
                matcher = pattern.matcher(email);
                return matcher.matches();

            }

            private boolean check_password_validity(String pass) {
                Pattern pattern;
                Matcher matcher;
                String ppattern = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{6,}$";
                pattern = Pattern.compile(ppattern);
                matcher = pattern.matcher(pass);
                return matcher.matches();

            }
        });


    }
}