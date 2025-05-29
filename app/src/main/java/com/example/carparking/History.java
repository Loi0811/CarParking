package com.example.carparking;

public class History {
    Integer in_or_out;
    String time;
    String plate;
    String token;
    String imageUrl;

    public History(Integer in_or_out, String time, String id, String plate, Boolean first_time) {
        this.in_or_out = in_or_out;
        this.time = time;
        this.plate = plate;
    }

    public Integer getIn_or_out() {
        return in_or_out;
    }

    public String getTime() {
        return time;
    }

    public String getPlate() {
        return plate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

