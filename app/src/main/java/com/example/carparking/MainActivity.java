package com.example.carparking;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private List<Vehicle> vehicleList = new ArrayList<>();   // KHÔNG để null
    private List<History> historyList = new ArrayList<>();
    private Fragment parkingFragment, streamCameraFragment, historyFragment;
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parkingFragment = new ParkingFragment();
        streamCameraFragment = new StreamCameraFragment();
        historyFragment = new HistoryFragment();

        fetchVehicleListFromAPI();
        fetchHistoryListFromAPI();

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

    private void fetchVehicleListFromAPI() {
        Request request = new Request.Builder()
                .url(ApiConfig.BASE_URL + "/parking/parking-list") // Ví dụ URL
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    vehicleList = gson.fromJson(json, new TypeToken<List<Vehicle>>(){}.getType());
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "Đã lấy danh sách xe", Toast.LENGTH_SHORT).show();
                        ParkingFragment pf = (ParkingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                        if (pf != null) {
                            pf.updateList(vehicleList);
                        }
                    });
                }
            }
        });
    }

    private void fetchHistoryListFromAPI() {
        Request request = new Request.Builder()
                .url(ApiConfig.BASE_URL + "/history/history-list") // Ví dụ URL
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    historyList = gson.fromJson(json, new TypeToken<List<History>>(){}.getType());
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "Đã lấy lịch sử", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }


    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public List<History> getHistoryList() {
        return historyList;
    }

    public void setVehicleList(List<Vehicle> list) {
        this.vehicleList = list;
    }

    public void setHistoryList(List<History> list) {
        this.historyList = list;
    }

}
