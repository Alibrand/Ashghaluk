package com.ksacp2022.ashghaluk;

public class Account {
    String id="";
    String name="";
    String phone="";
    String region="";
    String type="User";
    String service_category="";
    String facebook_account="";
    String twitter_account="";
    String snapchat_account="";
    String service_description="";
    String status="Active";
    String rating="0";
    String reviews_count="0";
    String price="";
    String total_requests="0";
    String completed_requests="0";

    public Account() {
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getReviews_count() {
        return reviews_count;
    }

    public void setReviews_count(String reviews_count) {
        this.reviews_count = reviews_count;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getService_category() {
        return service_category;
    }

    public void setService_category(String service_category) {
        this.service_category = service_category;
    }

    public String getFacebook_account() {
        return facebook_account;
    }

    public void setFacebook_account(String facebook_account) {
        this.facebook_account = facebook_account;
    }

    public String getTwitter_account() {
        return twitter_account;
    }

    public void setTwitter_account(String twitter_account) {
        this.twitter_account = twitter_account;
    }

    public String getSnapchat_account() {
        return snapchat_account;
    }

    public void setSnapchat_account(String snapchat_account) {
        this.snapchat_account = snapchat_account;
    }

    public String getService_description() {
        return service_description;
    }

    public void setService_description(String service_description) {
        this.service_description = service_description;
    }

    public String getTotal_requests() {
        return total_requests;
    }

    public void setTotal_requests(String total_requests) {
        this.total_requests = total_requests;
    }

    public String getCompleted_requests() {
        return completed_requests;
    }

    public void setCompleted_requests(String completed_requests) {
        this.completed_requests = completed_requests;
    }
}
