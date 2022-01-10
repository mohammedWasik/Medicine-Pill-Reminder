package com.example.medicinepillreminderapp;

import android.net.Uri;

public class Model {
    String name,dose,stock,id,date,time,startDate,endDate;
    int broadCast;
    String img;
    public Model(){

    }
    public Model(String name, String dose, String stock, String id, String date, String time, String startDate, String endDate, String img) {
        this.name = name;
        this.dose = dose;
        this.stock = stock;
        this.id = id;
        this.date = date;
        this.time = time;
        this.startDate = startDate;
        this.endDate = endDate;
        this.img = img;
    }





    public Model(String name, String dose, String stock, String id, String date, String time, String startDate, String endDate, String img, int broadCast) {
        this.name = name;
        this.dose = dose;
        this.stock = stock;
        this.id = id;
        this.date = date;
        this.time = time;
        this.startDate = startDate;
        this.endDate = endDate;
        this.img = img;
        this.broadCast=broadCast;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public int getBroadCast() {
        return broadCast;
    }

    public void setBroadCast(int broadCast) {
        this.broadCast = broadCast;
    }
}
