package com.ksacp2022.ashghaluk;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.style.MaskFilterSpan;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class TechnicianAccountActivity extends AppCompatActivity {


    TextView name, category, service_description, region,price,completion;
    AppCompatButton button_call, button_chat,button_facebook,button_twitter,button_reviews,
    button_request;
    RatingBar rating;
    FirebaseFirestore firestore;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_account);

        name = findViewById(R.id.name);
        category = findViewById(R.id.category);
        service_description = findViewById(R.id.service_description);
        region = findViewById(R.id.region);
        button_call = findViewById(R.id.button_call);
        button_chat = findViewById(R.id.button_chat);
        button_facebook = findViewById(R.id.button_facebook);
        button_twitter = findViewById(R.id.button_twitter);
        button_reviews=findViewById(R.id.button_reviews);
        rating = findViewById(R.id.rating);
        button_request = findViewById(R.id.button_request);
        price = findViewById(R.id.price);
        completion = findViewById(R.id.completion);






        String tech_id = getIntent().getStringExtra("tech_id");

        firestore = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Loading Account");
        dialog.show();


        firestore.collection("accounts")
                .document(tech_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()) {

                            DocumentSnapshot doc = task.getResult();
                            Map<String, Object> data = doc.getData();
                            name.setText(data.get("name").toString());
                            category.setText(data.get("service_category").toString());
                            service_description.setText(data.get("service_description").toString());
                            region.setText(data.get("region").toString());
                            price.setText(data.get("price").toString());

                            String rate=data.get("rating").toString();
                            float rate_value=Float.parseFloat(rate);
                            rating.setRating(rate_value);
                            String reviews_count=data.get("reviews_count").toString();
                            button_reviews.setText("Reviews ("+reviews_count+")");

                            String requested="0",completed="0";

                             if(data.get("total_requests")!=null)
                                 requested= data.get("total_requests").toString();
                            if(data.get("completed_requests")!=null)
                                completed= data.get("completed_requests").toString();

                            completion.setText(completed +" done / "+ requested+" requested");


                            button_call.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + data.get("phone").toString()));
                                    startActivity(intent);
                                }
                            });

                            button_facebook.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(data.get("facebook_account").toString().equals(""))
                                    {
                                        makeText(TechnicianAccountActivity.this, "This technician doesn't have a Facebook Account", LENGTH_LONG).show();

                                    }
                                    else{
                                        try{
                                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"+ data.get("facebook_account").toString()));
                                        startActivity(intent);
                                    }
                                    catch (Exception ex){
                                        makeText(TechnicianAccountActivity.this, "Failed to open account ", LENGTH_LONG).show();

                                    }
                                    }
                                }
                            });

                            button_twitter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(data.get("twitter_account").toString().equals(""))
                                    {
                                        makeText(TechnicianAccountActivity.this, "This technician doesn't have a Twitter Account", LENGTH_LONG).show();

                                    }
                                    else{
                                        try{
                                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com/"+data.get("twitter_account").toString()));
                                        startActivity(intent);}
                                        catch (Exception ex){
                                            makeText(TechnicianAccountActivity.this, "Failed to open account ", LENGTH_LONG).show();

                                        }
                                    }
                                }
                            });
                            button_reviews.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                        Intent intent = new Intent(TechnicianAccountActivity.this, TechnicianReviewsActivity.
                                                class);
                                        intent.putExtra("tech_id", tech_id);
                                        startActivity(intent);

                                }
                            });

                            button_chat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(TechnicianAccountActivity.this,ChatActivity.
                                    class);
                                    intent.putExtra("receiver_id",tech_id);
                                    intent.putExtra("receiver_name",data.get("name").toString());
                                    startActivity(intent);
                                }
                            });

                            button_request.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(TechnicianAccountActivity.this,RequestServiceActivity.
                                    class);
                                    intent.putExtra("tech_id",tech_id);
                                    intent.putExtra("tech_name",name.getText().toString());
                                    intent.putExtra("price",price.getText().toString());
                                    startActivity(intent);
                                }
                            });






                        } else {
                            makeText(TechnicianAccountActivity.this, "Failed to load account", LENGTH_LONG).show();
                            finish();
                        }
                    }

                });


    }
}