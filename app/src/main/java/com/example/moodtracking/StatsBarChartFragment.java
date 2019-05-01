package com.example.moodtracking;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.w3c.dom.Node;

import org.xml.sax.SAXException;


import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Calendar;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;


public class StatsBarChartFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_stats_bar_chart, container, false);

        TextView date_2 = (TextView) relativeLayout.findViewById(R.id.date_2);
        TextView tv_month = (TextView) relativeLayout.findViewById(R.id.textViewMonth);
        BarChart barChart_2 = (BarChart) relativeLayout.findViewById(R.id.barchart_2);
        Button pie_chart_button = (Button) relativeLayout.findViewById(R.id.pieChartButton);

        String date = new SimpleDateFormat("EEE d MMM yyyy", Locale.getDefault()).format(new Date());
        date_2.setText("" + date);

        String month = new SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(new Date());
        tv_month.setText("" + month);

        pie_chart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new StatsFragment();
                replaceFragment(newFragment);
            }
        });

        //Bar Chart
        barChart_2.setDrawBarShadow(false);
        barChart_2.setDrawValueAboveBar(true);
        barChart_2.setMaxVisibleValueCount(600);
        barChart_2.setPinchZoom(false);
        barChart_2.getDescription().setEnabled(false);
        barChart_2.getLegend().setEnabled(false);

        ArrayList<BarEntry> barEntriesSleep = new ArrayList<>();
        ArrayList<BarEntry> barEntriesExercise = new ArrayList<>();

        //BarDataSets
        BarDataSet barDataSetExercise = new BarDataSet(barEntriesExercise, "Exercise");

        barEntriesExercise.add(new BarEntry(0, 40f));
        barEntriesExercise.add(new BarEntry(1, 30f));
        barEntriesExercise.add(new BarEntry(2, 20f));
        barEntriesExercise.add(new BarEntry(3, 46f));
        barEntriesExercise.add(new BarEntry(4, 40f));
        barEntriesExercise.add(new BarEntry(5, 30f));
        barEntriesExercise.add(new BarEntry(6, 20f));
        barEntriesExercise.add(new BarEntry(7, 46f));

        barEntriesSleep.add(new BarEntry(0, 600f));
        barEntriesSleep.add(new BarEntry(1, 500f));
        barEntriesSleep.add(new BarEntry(2, 400f));
        barEntriesSleep.add(new BarEntry(3, 450f));
        barEntriesSleep.add(new BarEntry(4, 600f));
        barEntriesSleep.add(new BarEntry(5, 500f));
        barEntriesSleep.add(new BarEntry(6, 400f));
        barEntriesSleep.add(new BarEntry(7, 450f));

        barDataSetExercise.setColors(Color.rgb(242, 162, 162));
        BarDataSet barDataSetSleep = new BarDataSet(barEntriesSleep, "Sleep");
        barDataSetSleep.setColors(Color.rgb(136, 139, 221));
        barDataSetSleep.setValueFormatter(new SleepValueFormatter());

        barDataSetExercise.setValueTextSize(14f);
        barDataSetSleep.setValueTextSize(14f);

        BarData data = new BarData(barDataSetExercise, barDataSetSleep);

        float groupSpace = 0.5f;
        float barSpace = 0.13f;
        float barWidth = 0.12f;

        barChart_2.setData(data);
        data.setBarWidth(barWidth);
        barChart_2.groupBars(1, groupSpace, barSpace);

        //X-axis
        XAxis xAxis = barChart_2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        barChart_2.getXAxis().setGranularityEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(1f);
        xAxis.setAxisMaximum(31);
        xAxis.setValueFormatter(new dateFormatter());
        xAxis.setTextSize(16f);
        barChart_2.setExtraOffsets(0,0,20,12);


        //Y-axis
        YAxis yAxisLeft = barChart_2.getAxisLeft();
        YAxis yAxisRight = barChart_2.getAxisRight();
        yAxisLeft.setEnabled(false);
        yAxisRight.setEnabled(false);
        yAxisLeft.setDrawGridLines(false);

        //Scrolling
        barChart_2.setDragEnabled(true);
        barChart_2.setVisibleXRangeMaximum(5);
        barChart_2.moveViewToX(31); //TODO set to today
        barChart_2.invalidate();
        //barChart_2.get
    return relativeLayout;
    }

    public class SleepValueFormatter implements IValueFormatter
    {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler)
        {
            int totalMins = Math.round(value);
            int diffHours = totalMins/ 60;
            int diffMinutes = totalMins - (diffHours * 60);

            return diffHours + "h "+ diffMinutes +"m";
        }
    }

    public void replaceFragment (Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    public class dateFormatter implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("d");
            int val;
            val = Math.round(value);
            cal.add(Calendar.DATE, val);
            String date = dateFormat.format(cal.getTime());

            return date;
        }
    }
}
