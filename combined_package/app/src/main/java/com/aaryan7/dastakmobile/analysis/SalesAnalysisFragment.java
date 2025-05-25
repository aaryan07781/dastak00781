package com.aaryan7.dastakmobile.analysis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aaryan7.dastakmobile.R;
import com.aaryan7.dastakmobile.utils.DataRepository;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SalesAnalysisFragment extends Fragment {
    
    public static final String ARG_PERIOD = "period";
    public static final int PERIOD_DAILY = 0;
    public static final int PERIOD_WEEKLY = 1;
    public static final int PERIOD_MONTHLY = 2;
    public static final int PERIOD_YEARLY = 3;
    
    private int period;
    private BarChart chart;
    private TextView tvTotalSales;
    private DataRepository repository;
    
    public static SalesAnalysisFragment newInstance(int period) {
        SalesAnalysisFragment fragment = new SalesAnalysisFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PERIOD, period);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            period = getArguments().getInt(ARG_PERIOD);
        }
        repository = DataRepository.getInstance(requireContext());
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analysis, container, false);
        
        chart = view.findViewById(R.id.chart);
        tvTotalSales = view.findViewById(R.id.tvTotal);
        
        setupChart();
        loadData();
        
        return view;
    }
    
    private void setupChart() {
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
    }
    
    private void loadData() {
        Date currentDate = new Date();
        
        switch (period) {
            case PERIOD_DAILY:
                loadDailyData(currentDate);
                break;
            case PERIOD_WEEKLY:
                loadWeeklyData(currentDate);
                break;
            case PERIOD_MONTHLY:
                loadMonthlyData(currentDate);
                break;
            case PERIOD_YEARLY:
                loadYearlyData();
                break;
        }
    }
    
    private void loadDailyData(Date date) {
        // For daily view, we'll show hourly sales for the selected day
        repository.getDailySales(date, result -> {
            if (getActivity() == null) return;
            
            getActivity().runOnUiThread(() -> {
                tvTotalSales.setText(String.format(Locale.getDefault(), "Total Sales: ₹%.2f", result));
                
                // Create sample hourly data for demonstration
                // In a real app, you would query hourly sales from the database
                List<BarEntry> entries = new ArrayList<>();
                List<String> labels = new ArrayList<>();
                
                for (int i = 9; i <= 21; i++) { // 9 AM to 9 PM
                    float value = (float) (Math.random() * result / 13); // Distribute total randomly
                    entries.add(new BarEntry(i - 9, value));
                    labels.add(i + ":00");
                }
                
                BarDataSet dataSet = new BarDataSet(entries, "Sales");
                dataSet.setColor(getResources().getColor(R.color.colorPrimary));
                
                BarData data = new BarData(dataSet);
                data.setBarWidth(0.6f);
                
                chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                chart.setData(data);
                chart.invalidate();
            });
        });
    }
    
    private void loadWeeklyData(Date date) {
        repository.getWeeklySales(date, result -> {
            if (getActivity() == null) return;
            
            getActivity().runOnUiThread(() -> {
                tvTotalSales.setText(String.format(Locale.getDefault(), "Total Sales: ₹%.2f", result));
                
                // Create sample daily data for the week
                List<BarEntry> entries = new ArrayList<>();
                List<String> labels = new ArrayList<>();
                
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                
                SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.getDefault());
                
                for (int i = 0; i < 7; i++) {
                    float value = (float) (Math.random() * result / 7); // Distribute total randomly
                    entries.add(new BarEntry(i, value));
                    labels.add(sdf.format(calendar.getTime()));
                    calendar.add(Calendar.DAY_OF_WEEK, 1);
                }
                
                BarDataSet dataSet = new BarDataSet(entries, "Sales");
                dataSet.setColor(getResources().getColor(R.color.colorPrimary));
                
                BarData data = new BarData(dataSet);
                data.setBarWidth(0.6f);
                
                chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                chart.setData(data);
                chart.invalidate();
            });
        });
    }
    
    private void loadMonthlyData(Date date) {
        repository.getMonthlySales(date, result -> {
            if (getActivity() == null) return;
            
            getActivity().runOnUiThread(() -> {
                tvTotalSales.setText(String.format(Locale.getDefault(), "Total Sales: ₹%.2f", result));
                
                // Create sample weekly data for the month
                List<BarEntry> entries = new ArrayList<>();
                List<String> labels = new ArrayList<>();
                
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                
                int weeksInMonth = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
                
                for (int i = 0; i < weeksInMonth; i++) {
                    float value = (float) (Math.random() * result / weeksInMonth); // Distribute total randomly
                    entries.add(new BarEntry(i, value));
                    labels.add("Week " + (i + 1));
                }
                
                BarDataSet dataSet = new BarDataSet(entries, "Sales");
                dataSet.setColor(getResources().getColor(R.color.colorPrimary));
                
                BarData data = new BarData(dataSet);
                data.setBarWidth(0.6f);
                
                chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                chart.setData(data);
                chart.invalidate();
            });
        });
    }
    
    private void loadYearlyData() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        repository.getYearlySales(currentYear, result -> {
            if (getActivity() == null) return;
            
            getActivity().runOnUiThread(() -> {
                tvTotalSales.setText(String.format(Locale.getDefault(), "Total Sales: ₹%.2f", result));
                
                // Create sample monthly data for the year
                List<BarEntry> entries = new ArrayList<>();
                List<String> labels = new ArrayList<>();
                
                String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", 
                                              "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                
                for (int i = 0; i < 12; i++) {
                    float value = (float) (Math.random() * result / 12); // Distribute total randomly
                    entries.add(new BarEntry(i, value));
                    labels.add(months[i]);
                }
                
                BarDataSet dataSet = new BarDataSet(entries, "Sales");
                dataSet.setColor(getResources().getColor(R.color.colorPrimary));
                
                BarData data = new BarData(dataSet);
                data.setBarWidth(0.6f);
                
                chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                chart.setData(data);
                chart.invalidate();
            });
        });
    }
}
