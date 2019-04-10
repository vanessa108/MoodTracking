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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        TextView sleepDataText = (TextView) relativeLayout.findViewById(R.id.textViewDataSleep);
        BarChart barChart = (BarChart) relativeLayout.findViewById(R.id.barchart);

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

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

        proBar.setSecondaryProgress(80);
        return relativeLayout;

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

        //TODO add data from files
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0, 40f));
        barEntries.add(new BarEntry(1, 30f));
        barEntries.add(new BarEntry(2, 20f));
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
        float barSpace = 0.09f;
        float barWidth = 0.12f;

        barChart.setData(data);
        data.setBarWidth(barWidth);
        barChart.groupBars(1, groupSpace, barSpace);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yAxisLeft = barChart.getAxisLeft();
        YAxis yAxisRight = barChart.getAxisRight();

        //TODO do we want labels on Y axis?
        yAxisLeft.setEnabled(false);
        yAxisRight.setEnabled(false);

        yAxisLeft.setDrawGridLines(false);
        xAxis.setDrawGridLines(false);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barDataSet.setValueTextSize(14f);
        barDataSet1.setValueTextSize(14f);

        //TODO if we want a line in different colours for the mood
        //Paint paint = mChart.getRenderer().getPaintRender();
        //paint.setShader(new LinearGradient(0, 0, 0, 40, Color.YELLOW, Color.RED, Shader.TileMode.REPEAT));


        //Test of reading data
        sleepDataText.setText(MainActivity.getDataFromFile(getContext(), "trackingdata.txt"));

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



}
