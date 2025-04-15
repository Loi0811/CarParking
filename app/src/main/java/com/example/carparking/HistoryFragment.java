package com.example.carparking;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;




public class HistoryFragment extends Fragment {

    ListView listView;
    EditText searchInput;
    List<History> historyList;
    HistoryAdapter adapter;
    List<History> HistoryList;
    private String lastTimeFilter = "all";
    private boolean lastEntryFilter = true;
    private boolean lastExitFilter = true;
    private boolean lastFirstTimeFilter = false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        listView = view.findViewById(R.id.history_list);
        searchInput = view.findViewById(R.id.search_input);

        MainActivity mainActivity = (MainActivity) getActivity();
        historyList = mainActivity.getHistoryList();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss", Locale.getDefault());

        HistoryList = new ArrayList<>(historyList);

        Collections.sort(HistoryList, (h1, h2) -> {
            try {
                Date d1 = sdf.parse(h1.getTime());
                Date d2 = sdf.parse(h2.getTime());
                return d2.compareTo(d1);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        });

        adapter = new HistoryAdapter(getContext(), HistoryList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            History selectedHistory = adapter.getItem(position);
            showHistoryDetailDialog(selectedHistory);
        });


        searchInput.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}
        });

        ImageView filterButton = view.findViewById(R.id.filter);
        filterButton.setOnClickListener(v -> showFilterDialog());


        return view;
    }

    private void showFilterDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_filter, null);

        RadioGroup timeFilterGroup = dialogView.findViewById(R.id.time_filter_group);
        CheckBox checkboxEntry = dialogView.findViewById(R.id.checkbox_entry);
        CheckBox checkboxExit = dialogView.findViewById(R.id.checkbox_exit);
        CheckBox checkboxFirst = dialogView.findViewById(R.id.checkbox_first);
        Button btnApply = dialogView.findViewById(R.id.btn_apply_filter);

        // Gán lại lựa chọn trước đó
        switch (lastTimeFilter) {
            case "24h":
                timeFilterGroup.check(R.id.radio_24h);
                break;
            case "7d":
                timeFilterGroup.check(R.id.radio_7d);
                break;
            default:
                timeFilterGroup.check(R.id.radio_all);
                break;
        }
        checkboxEntry.setChecked(lastEntryFilter);
        checkboxExit.setChecked(lastExitFilter);
        checkboxFirst.setChecked(lastFirstTimeFilter);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Nền dialog trong suốt
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        btnApply.setOnClickListener(v -> {
            // Lưu lựa chọn
            int checkedId = timeFilterGroup.getCheckedRadioButtonId();
            if (checkedId == R.id.radio_24h) {
                lastTimeFilter = "24h";
            } else if (checkedId == R.id.radio_7d) {
                lastTimeFilter = "7d";
            } else {
                lastTimeFilter = "all";
            }

            lastEntryFilter = checkboxEntry.isChecked();
            lastExitFilter = checkboxExit.isChecked();
            lastFirstTimeFilter = checkboxFirst.isChecked();

            applyFilters(lastTimeFilter, lastEntryFilter, lastExitFilter, lastFirstTimeFilter);
            dialog.dismiss();
        });

        dialog.show();
    }


    private void applyFilters(String timeRange, boolean filterEntry, boolean filterExit, boolean filterFirst) {
        List<History> filteredList = new ArrayList<>();

        long currentTime = System.currentTimeMillis();
        long timeThreshold = 0;

        // Tính thời gian giới hạn nếu cần
        if (timeRange.equals("24h")) {
            timeThreshold = currentTime - (24 * 60 * 60 * 1000);
        } else if (timeRange.equals("7d")) {
            timeThreshold = currentTime - (7L * 24 * 60 * 60 * 1000);
        }

        for (History history : HistoryList) {
            long historyTime = parseTimeMillis(history.getTime());

            // Bỏ qua nếu ngoài khoảng thời gian yêu cầu
            if (timeThreshold > 0 && historyTime < timeThreshold) {
                continue;
            }

            // Lọc theo loại sự kiện
            if (!filterEntry && history.getIn_or_out() == 0) continue;
            if (!filterExit && history.getIn_or_out() == 1) continue;

            // Lọc lần đầu
            if (filterFirst && !history.getFirst_time()) continue;

            filteredList.add(history);
        }

        adapter.updateData(filteredList);
    }


    private long parseTimeMillis(String timeStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss", Locale.getDefault());
            Date date = sdf.parse(timeStr);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void showHistoryDetailDialog(History history) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_detail_history, null);


        TextView idView = dialogView.findViewById(R.id.id);
        TextView plateView = dialogView.findViewById(R.id.license_plate);
        TextView timeView = dialogView.findViewById(R.id.time);
        TextView actionText = dialogView.findViewById(R.id.action);
        ImageView actionImage = dialogView.findViewById(R.id.action_icon);
        Button btnClose = dialogView.findViewById(R.id.btn_close);

        idView.setText(history.getId());
        plateView.setText(history.getPlate());
        timeView.setText(history.getTime());


        if (history.getIn_or_out() == 0) {
            actionImage.setImageResource(R.drawable.ic_in);
            actionText.setText("Vào bãi xe");
        } else {
            actionImage.setImageResource(R.drawable.ic_out);
            actionText.setText("Ra khỏi bãi");
        }

        AlertDialog dialog = builder.setView(dialogView).create();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        btnClose.setOnClickListener(v -> dialog.dismiss());
    }

}


