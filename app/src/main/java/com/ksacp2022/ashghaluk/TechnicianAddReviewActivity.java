package com.ksacp2022.ashghaluk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.DocumentTransform;

public class TechnicianAddReviewActivity extends AppCompatActivity {

    AppCompatButton send;
    EditText full_name,service_review;
    RatingBar rating;

    FirebaseFirestore firestore;
    ProgressDialog dialog;

    String tech_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_add_review);
        send = findViewById(R.id.send);
        full_name = findViewById(R.id.full_name);
        service_review = findViewById(R.id.service_review);
        rating = findViewById(R.id.rating);


        tech_id=getIntent().getStringExtra("tech_id");



        firestore=FirebaseFirestore.getInstance();
        dialog=new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_full_name =full_name.getText().toString();
                String str_service_review =service_review.getText().toString();
                float rate=rating.getRating();

                if(str_full_name.isEmpty())
                {
                    full_name.setError("You should fill this field");
                    return;
                }
                if(str_service_review.isEmpty())
                {
                    service_review.setError("You should fill this field");
                    return;
                }

                Review review=new Review();
                review.setReview(str_service_review);
                review.setName(str_full_name);
                review.setRating(String.valueOf(rate));
                review.setCreate_date(null);

                dialog.setMessage("Saving Review");
                dialog.show();


                firestore.collection("accounts")
                        .document(tech_id)
                        .collection("reviews")
                        .add(review)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                if(task.isSuccessful())
                                {
                                    Toast.makeText(TechnicianAddReviewActivity.this,"Review saved successfully" , Toast.LENGTH_LONG).show();
                                    //re calculate rating
                                    firestore.collection("accounts")
                                            .document(tech_id)
                                            .collection("reviews")
                                                    .get()
                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    //calculate rating
                                                                    int reviews_count=task.getResult().getDocuments().size();
                                                                    float sum=0;
                                                                    for (DocumentSnapshot doc:task.getResult().getDocuments()
                                                                         ) {
                                                                        String rate=doc.getData().get("rating").toString();
                                                                        float rating=Float.parseFloat(rate);
                                                                        sum+=rating;
                                                                    }
                                                                    float rate=sum/reviews_count;
                                                                    //update techincian profile
                                                                    firestore.collection("accounts")
                                                                            .document(tech_id)
                                                                            .update("rating",rate,"reviews_count",reviews_count)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    dialog.dismiss();
                                                                                    finish();
                                                                                }
                                                                            });

                                                                }
                                                            });

                                }else{
                                    Toast.makeText(TechnicianAddReviewActivity.this,"Failed to save review" , Toast.LENGTH_LONG).show();

                                }
                            }
                        });



            }
        });
    }
}