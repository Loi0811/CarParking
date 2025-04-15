package com.example.carparking;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private List<Vehicle> vehicleList;
    private List<History> historyList;
    private Fragment parkingFragment, streamCameraFragment, historyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parkingFragment = new ParkingFragment();
        streamCameraFragment = new StreamCameraFragment();
        historyFragment = new HistoryFragment();

        vehicleList = Arrays.asList(
                new Vehicle("Car001", "29-AB 12345"),
                new Vehicle("Car002", "30-A1 23456"),
                new Vehicle("Car003", "59-A1 21456")
        );

        historyList = Arrays.asList(
                new History(0, "12-4-2025T08:00:00", "Car001", "29-AB 12345",true),
                new History(0, "12-4-2025T10:30:00", "Car002", "30-A1 23456",false),
                new History(1, "12-4-2025T12:15:00", "Car001", "29-AB 12345",false),
                new History(0, "12-4-2025T14:15:00", "Car003", "59-A1 21456",true),
                new History(0, "12-4-2025T14:30:00", "Car001", "29-AB 12345",false),
                new History(0, "10-4-2025T08:00:00", "Car004", "29-AB 12305",true),
                new History(1, "10-4-2025T10:00:00", "Car004", "29-AB 12305",false),
                new History(0, "14-4-2025T10:00:00", "Car005", "74-AB 10305",true),
                new History(1, "14-4-2025T12:00:00", "Car005", "74-AB 10305",false)
        );

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        loadFragment(new ParkingFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment;
            if (item.getItemId() == R.id.nav_parking) {
                fragment = parkingFragment;
            } else if (item.getItemId() == R.id.nav_camera) {
                fragment = streamCameraFragment;
            } else {
                fragment = historyFragment;
            }
            loadFragment(fragment);
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public List<History> getHistoryList() {
        return historyList;
    }

}
