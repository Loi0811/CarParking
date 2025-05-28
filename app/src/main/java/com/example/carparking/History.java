package com.example.carparking;

public class History {
    Integer in_or_out;
    String time;
    String id;
    String plate;
    Boolean first_time;
    String token;
    String imageUrl;

    public History(Integer in_or_out, String time, String id, String plate, Boolean first_time) {
        this.in_or_out = in_or_out;
        this.time = time;
        this.id = id;
        this.plate = plate;
        this.first_time = first_time;
    }

    public Integer getIn_or_out() {
        return in_or_out;
    }

    public String getTime() {
        return time;
    }

    public String getId() {
        return id;
    }

    public String getPlate() {
        return plate;
    }

    public Boolean getFirst_time() {
        return first_time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

