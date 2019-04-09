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

public class StatsFragment extends Fragment {

    private PieChart exerciseChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_stats, container, false);

        PieChart exerciseChart = (PieChart) relativeLayout.findViewById(R.id.exerciseChart);
        exerciseChart.setBackgroundColor(Color.WHITE);

        moveOffScreen();



        return relativeLayout;
    }





    private void moveOffScreen() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;
        int offset = (int) (height * 0.5);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) exerciseChart.getLayoutParams();
        params.setMargins(0,0,0, -offset);
        exerciseChart.setLayoutParams(params);

    }
}
