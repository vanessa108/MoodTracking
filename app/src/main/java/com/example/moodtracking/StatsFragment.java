package com.example.moodtracking;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class StatsFragment extends Fragment {

    private PieChart exerciseChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_stats, container, false);

        PieChart exerciseChart = (PieChart) relativeLayout.findViewById(R.id.exerciseChart);


        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(18.5f, "Super sad"));
        entries.add(new PieEntry(26.7f, "Sad"));
        entries.add(new PieEntry(12f, "Neutral"));
        entries.add(new PieEntry(12f, "Happy"));
        entries.add(new PieEntry(12f, "Super Happy"));

        PieDataSet set = new PieDataSet(entries, "Test");
        PieData data = new PieData(set);
        exerciseChart.setMaxAngle(180f);
        exerciseChart.setRotation(270f);
        set.setSliceSpace(5);
        set.setColor(Color.MAGENTA);
        exerciseChart.setData(data);
        exerciseChart.invalidate();

        return relativeLayout;
    }



}
