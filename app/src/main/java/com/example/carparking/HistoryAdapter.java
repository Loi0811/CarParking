package com.example.carparking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends BaseAdapter implements Filterable {

    Context context;
    List<History> originalList;
    List<History> filteredList;


    public HistoryAdapter(Context context, List<History> data) {
        this.context = context;
        this.originalList = data;
        this.filteredList = data;
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public History getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);

        ImageView tvIcon = convertView.findViewById(R.id.tv_icon);
        TextView tvTime = convertView.findViewById(R.id.tv_time);
        TextView tvInfo = convertView.findViewById(R.id.tv_vehicle_info);

        History history = filteredList.get(pos);
        if (history.in_or_out == 0) tvIcon.setImageResource(R.drawable.ic_in);
        else tvIcon.setImageResource(R.drawable.ic_out);
        tvTime.setText(history.time);
        tvInfo.setText("Biến số: " + history.plate);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            protected FilterResults performFiltering(CharSequence constraint) {
                List<History> filtered = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filtered = originalList;
                } else {
                    String keyword = constraint.toString().toLowerCase();
                    for (History h : originalList) {
                        if (h.plate.toLowerCase().contains(keyword)) {
                            filtered.add(h);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }

            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (List<History>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void updateData(List<History> newData) {
        this.originalList = new ArrayList<>(newData);
        this.filteredList = new ArrayList<>(newData);
        notifyDataSetChanged();
    }

}

