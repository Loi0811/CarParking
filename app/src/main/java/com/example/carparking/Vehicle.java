package com.example.carparking;

import com.google.gson.annotations.SerializedName;

public class Vehicle {

    String plate;
    String token;

    // Constructor mặc định (bắt buộc cho Gson)
    public Vehicle() {}

    // Constructor dùng khi cần khởi tạo thủ công
    public Vehicle(String plate, String token) {
        this.plate = plate;
        this.token = token;
    }
}


