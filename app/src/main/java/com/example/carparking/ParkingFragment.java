package com.example.carparking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParkingFragment extends Fragment {

    ListView listView;
    TextView countText;
    List<Vehicle> vehicles;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking, container, false);
        listView = view.findViewById(R.id.parking_list);
        countText = view.findViewById(R.id.count_text);

        MainActivity mainActivity = (MainActivity) getActivity();
        vehicles = mainActivity.getVehicleList();


        VehicleAdapter adapter = new VehicleAdapter(getContext(), vehicles);
        listView.setAdapter(adapter);
        countText.setText("Tổng số xe: " + vehicles.size());

        return view;
    }
}


