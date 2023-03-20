package com.ksacp2022.ashghaluk;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Review {
    String name;
    String review;
    String rating;
    @ServerTimestamp
    Date create_date;

    public Review() {
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
