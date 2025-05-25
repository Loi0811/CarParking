package com.example.carparking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ParkingFragment extends Fragment {

    ListView listView;
    TextView countText;
    List<Vehicle> vehicles;
    private Button btnRefresh; // Thêm biến toàn cục cho nút refresh

    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking, container, false);
        listView = view.findViewById(R.id.parking_list);
        countText = view.findViewById(R.id.count_text);
        btnRefresh = view.findViewById(R.id.btnRefresh);

        MainActivity mainActivity = (MainActivity) getActivity();
        vehicles = mainActivity.getVehicleList();

        VehicleAdapter adapter = new VehicleAdapter(getContext(), vehicles);
        listView.setAdapter(adapter);
        countText.setText("Total: " + vehicles.size());

        btnRefresh.setOnClickListener(v -> {
            btnRefresh.setEnabled(false); // 🔒 Vô hiệu hóa khi đang tải
            fetchVehicleList();
        });

        return view;
    }

    public void updateList(List<Vehicle> newVehicles) {
        this.vehicles = newVehicles;
        if (listView != null && getContext() != null) {
            VehicleAdapter adapter = new VehicleAdapter(getContext(), vehicles);
            listView.setAdapter(adapter);
            countText.setText("Total: " + vehicles.size());
        }
    }

    private void fetchVehicleList() {
        Request request = new Request.Builder()
                .url(ApiConfig.BASE_URL + "/parking/parking-list")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Không thể kết nối server", Toast.LENGTH_SHORT).show();
                        btnRefresh.setEnabled(true); // 🔓 Bật lại nút khi thất bại
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    List<Vehicle> newVehicles = gson.fromJson(json, new TypeToken<List<Vehicle>>(){}.getType());

                    getActivity().runOnUiThread(() -> {
                        vehicles = newVehicles;
                        ((MainActivity)getActivity()).setVehicleList(newVehicles);
                        VehicleAdapter adapter = new VehicleAdapter(getContext(), vehicles);
                        listView.setAdapter(adapter);
                        countText.setText("Total: " + vehicles.size());
                        btnRefresh.setEnabled(true); // 🔓 Bật lại nút khi thành công
                    });
                } else {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Lỗi từ server", Toast.LENGTH_SHORT).show();
                            btnRefresh.setEnabled(true); // 🔓 Bật lại nút khi có lỗi từ server
                        });
                    }
                }
            }
        });
    }
}



