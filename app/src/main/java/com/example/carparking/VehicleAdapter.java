package com.example.carparking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class VehicleAdapter extends BaseAdapter {

    Context context;
    List<Vehicle> vehicles;

    public VehicleAdapter(Context context, List<Vehicle> vehicles) {
        this.context = context;
        this.vehicles = vehicles;
    }

    @Override
    public int getCount() {
        return vehicles == null ? 0 : vehicles.size();
    }

    @Override
    public Object getItem(int position) {
        return vehicles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_vehicle, parent, false);

        TextView tvPlate = convertView.findViewById(R.id.tv_plate_number);

        Vehicle vehicle = vehicles.get(pos);
        tvPlate.setText("Biển số: " + vehicle.plate);

        return convertView;
    }
}
