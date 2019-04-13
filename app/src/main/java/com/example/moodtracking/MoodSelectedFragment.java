package com.example.moodtracking;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;




public class MoodSelectedFragment extends Fragment {

    static int selectedMood;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_mood_selected, container, false);

        ImageView whichMood = (ImageView) relativeLayout.findViewById(R.id.selectedMoodIMG);
        ImageButton editPen = (ImageButton) relativeLayout.findViewById(R.id.editPenButton);
        TextView whichMoodText = (TextView) relativeLayout.findViewById(R.id.textViewMood);
        TextView dateText = (TextView) relativeLayout.findViewById(R.id.date);
        ProgressBar proBar = (ProgressBar) relativeLayout.findViewById(R.id.sleepCircle);
        BarChart barChart = (BarChart) relativeLayout.findViewById(R.id.barchart);
        TextView startSleep = (TextView) relativeLayout.findViewById(R.id.StartBedTime);
        TextView endSleep = (TextView) relativeLayout.findViewById(R.id.EndBedTime);
        TextView yesterdayText = (TextView) relativeLayout.findViewById(R.id.textViewYesterday);
        TextView twoDaysAgoText = (TextView) relativeLayout.findViewById(R.id.textViewTwoDaysAgo);
        TextView threeDaysAgoText = (TextView) relativeLayout.findViewById(R.id.textViewThreeDaysAgo);

        String date = new SimpleDateFormat("EEE dd/MM", Locale.getDefault()).format(new Date());

        dateText.setText(date);

        if (selectedMood == 1) {
            whichMood.setImageResource(R.drawable.supersad);
            whichMoodText.setText("Awful");
        } else if (selectedMood == 2) {
            whichMood.setImageResource(R.drawable.sad);
            whichMoodText.setText("Rough");
        } else if (selectedMood == 3) {
            whichMood.setImageResource(R.drawable.neutral);
            whichMoodText.setText("Ok");
        } else if (selectedMood == 4) {
            whichMood.setImageResource(R.drawable.happy);
            whichMoodText.setText("Good");
        } else if (selectedMood == 5) {
            whichMood.setImageResource(R.drawable.superhappy);
            whichMoodText.setText("Awesome");
        }

        //Create function to calculate the start angle and the progress based on the time
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String date1 = "2019/04/09 23:00:00";
        String date2 = "2019/04/10 6:00:00";
        Date start = null;
        Date end = null;
        try {
            start = format.parse(date1);
            end = format.parse(date2);

        } catch (Exception e) {
            e.printStackTrace();
        }
        startSleep.setText(getTime(start));
        endSleep.setText(getTime(end));

        proBar.setSecondaryProgress(calcProgress(start,end));
        //set text values  based on the start time and end time of the sleep data


        editPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new HomeFragment();
                replaceFragment(newFragment);
            }
        });

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0, 40f));
        barEntries.add(new BarEntry(1, 30f));
        barEntries.add(new BarEntry(2, 22f));
        barEntries.add(new BarEntry(3, 46f));

        ArrayList<BarEntry> barEntries1 = new ArrayList<>();
        barEntries1.add(new BarEntry(0, 30f));
        barEntries1.add(new BarEntry(1, 21f));
        barEntries1.add(new BarEntry(2, 22f));
        barEntries1.add(new BarEntry(3, 46f));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Exercise");
        barDataSet.setColors(Color.rgb(242, 162, 162));

        BarDataSet barDataSet1 = new BarDataSet(barEntries1, "Sleep");
        barDataSet1.setColors(Color.rgb(136, 139, 221));

        BarData data = new BarData(barDataSet, barDataSet1);

        float groupSpace = 0.3f;
        float barSpace = 0.16f;
        float barWidth = 0.12f;

        barChart.setData(data);
        data.setBarWidth(barWidth);
        barChart.groupBars(1, groupSpace, barSpace);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barDataSet.setValueTextSize(14f);
        barDataSet1.setValueTextSize(14f);

        //X-axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(0.1f);
        xAxis.setGranularityEnabled(true);

        //Yesterday
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("EEE dd/MM");
        cal.add(Calendar.DATE, -1);
        yesterdayText.setText(dateFormat.format(cal.getTime()));

        //The day before yesterday
        Calendar cal2 = Calendar.getInstance();
        cal.add(Calendar.DATE, - 2);
        twoDaysAgoText.setText(dateFormat.format(cal2.getTime()));

        //Two days before yesterday
        Calendar cal3 = Calendar.getInstance();
        cal3.add(Calendar.DATE, - 3);
        threeDaysAgoText.setText(dateFormat.format(cal3.getTime()));

        //TODO get IndexValueFormatter to work...
        String[] values = new String[] {"one", "two", "three"};
        //xAxis.setValueFormatter(new MyXAxisValueFormatter(values));

        //Y-axis
        YAxis yAxisLeft = barChart.getAxisLeft();
        YAxis yAxisRight = barChart.getAxisRight();
        yAxisLeft.setEnabled(false);
        yAxisRight.setEnabled(false);
        yAxisLeft.setDrawGridLines(false);

        return relativeLayout;
    }

    public static void setParameters(int mood) {
        MoodSelectedFragment.selectedMood = mood;
    }
    private void replaceFragment (Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
    private int calcStartAngleProgressBar(){
        return 0;
    }
    private int calcProgress(Date start,Date end){
        long diff = end.getTime() - start.getTime();
        /* remove the milliseconds part */
        diff = diff / 1000;
        long hours = diff / (60 * 60) % 24;
        return (int)hours;
    }
    private String getTime(Date in){
        String formattedDate = new SimpleDateFormat("HH:mm").format(in);
        return formattedDate;
    }



    //public class MyXAxisValueFormatter implements IndexAxisValueFormatter {
    }
