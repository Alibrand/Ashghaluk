package com.ksacp2022.ashghaluk;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class ServiceRequest {
    String id;
    String technician_name;
    String technician_id;
    String client_name;
    String client_id;
    String price;
    @ServerTimestamp
    Date request_date;
    String status="Pending";

    public ServiceRequest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTechnician_name() {
        return technician_name;
    }

    public void setTechnician_name(String technician_name) {
        this.technician_name = technician_name;
    }

    public String getTechnician_id() {
        return technician_id;
    }

    public void setTechnician_id(String technician_id) {
        this.technician_id = technician_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public Date getRequest_date() {
        return request_date;
    }

    public void setRequest_date(Date request_date) {
        this.request_date = request_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
