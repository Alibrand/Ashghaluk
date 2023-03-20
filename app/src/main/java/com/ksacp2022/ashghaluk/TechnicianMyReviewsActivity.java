package com.ksacp2022.ashghaluk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.RatingBar;
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

public class TechnicianMyReviewsActivity extends AppCompatActivity {
    RecyclerView reviews;
    List<Review> reviewsList;
    RatingBar rating;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_my_reviews);
        reviews = findViewById(R.id.reviews);
        rating = findViewById(R.id.rating);

        firestore=FirebaseFirestore.getInstance();
        dialog=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();

        String tech_id= firebaseAuth.getUid();;

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

                            TechnicianReviewsAdapter adapter=new TechnicianReviewsAdapter(reviewsList,TechnicianMyReviewsActivity.this);
                            reviews.setAdapter(adapter);

                            firestore.collection("accounts")
                                    .document(tech_id)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            dialog.dismiss();
                                            if(task.isSuccessful())
                                        {

                                            Map<String,Object> data=task.getResult().getData();
                                            String rate=data.get("rating").toString();
                                            float rate_value=Float.parseFloat(rate);
                                            rating.setRating(rate_value);


                                        }
                                        }
                                    });


                        }else{
                            Toast.makeText(TechnicianMyReviewsActivity.this,"Failed to load Reviews" , Toast.LENGTH_LONG).show();

                        }

                    }
                });


    }
}