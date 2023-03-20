package com.ksacp2022.ashghaluk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TechnicianReviewsActivity extends AppCompatActivity {

    AppCompatButton add_review;
    RecyclerView reviews;
    List<Review> reviewsList;

    FirebaseFirestore firestore;
    ProgressDialog dialog;
    String tech_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_reviews);
        add_review = findViewById(R.id.add_review);
        reviews = findViewById(R.id.reviews);

        firestore=FirebaseFirestore.getInstance();
        dialog=new ProgressDialog(this);

        tech_id=getIntent().getStringExtra("tech_id");

        load_reviews();


        add_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TechnicianReviewsActivity.this,TechnicianAddReviewActivity. class);
                intent.putExtra("tech_id",tech_id);
                startActivity(intent);
            }
        });






    }

    private void load_reviews() {
        dialog.setMessage("Loading Reviews");
        dialog.show();
        firestore.collection("accounts")
                .document(tech_id)
                .collection("reviews")
                .orderBy("create_date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        reviewsList=new ArrayList<Review>();
                        dialog.dismiss();
                        if(task.isSuccessful())
                        {
                            for (DocumentSnapshot rev:task.getResult().getDocuments()
                            ) {
                                Map<String,Object> data=rev.getData();
                                Review review=new Review();
                                review.setName(data.get("name").toString());
                                review.setReview(data.get("review").toString());
                                review.setRating(data.get("rating").toString());
                                reviewsList.add(review);
                            }

                            TechnicianReviewsAdapter adapter=new TechnicianReviewsAdapter(reviewsList,TechnicianReviewsActivity.this);
                            reviews.setAdapter(adapter);


                        }else{
                            Toast.makeText(TechnicianReviewsActivity.this,"Failed to load Reviews" , Toast.LENGTH_LONG).show();

                        }

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        load_reviews();
    }
}