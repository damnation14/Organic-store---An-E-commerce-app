package com.example.organicstoreapp.Model;

public class UserOrders {
    private  String Address,Name,Order_Status,Pincode,Total_Amount,date,time,Phone;
    public UserOrders(){
    }

    public String getPhone(){
        return Phone;
    }
    public void setPhone(String phone){
        Phone=phone;
    }
    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getOrder_Status() {
        return Order_Status;
    }

    public void setOrder_Status(String order_Status) {
        Order_Status = order_Status;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getTotal_Amount() {
        return Total_Amount;
    }

    public void setTotal_Amount(String total_Amount) {
        Total_Amount = total_Amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public UserOrders(String address, String name,String phone, String order_status, String pincode, String total_Amount, String date, String time) {
        Address = address;
        Name = name;
        Phone=phone;
        Order_Status = order_status;
        Pincode = pincode;
        Total_Amount = total_Amount;
        this.date = date;
        this.time = time;
    }
}