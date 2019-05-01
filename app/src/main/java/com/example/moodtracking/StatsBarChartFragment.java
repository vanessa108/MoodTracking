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
import java.util.Collections;
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
        TextView textViewSleep_2 = (TextView) relativeLayout.findViewById(R.id.textViewSleep_2);
        TextView tv_month = (TextView) relativeLayout.findViewById(R.id.textViewMonth);
        TextView textViewExercise_2 = (TextView) relativeLayout.findViewById(R.id.textViewExercise_2);
        BarChart barChart_2 = (BarChart) relativeLayout.findViewById(R.id.barchart_2);
        Button pie_chart_button = (Button) relativeLayout.findViewById(R.id.pieChartButton);

        TextView startSleep_2 = (TextView) relativeLayout.findViewById(R.id.StartBedTime_2);
        List<TextView> sSleep_2 = Arrays.asList(startSleep_2);
        TextView endSleep_2 = (TextView) relativeLayout.findViewById(R.id.EndBedTime_2);
        ProgressBar proBar_2 = (ProgressBar) relativeLayout.findViewById(R.id.sleepCircle_2);

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


        /*Read data*/
        InputStream exp = getResources().openRawResource(R.raw.export);
        InputStream mod = getResources().openRawResource(R.raw.mooddata);
        HealthData hd = new HealthData(exp,mod);
        List<extData> sd = null;
        List<extData> md = null;
        List<extData> ad = null;

        try {
            int lastdays = 31;
            //get the data from the "lasdays" Order of data is today-n -> reverse data for bars after adding it to the barchart
            sd = hd.getSleepData(lastdays);
            md = hd.getMoodData(lastdays);
            ad = hd.getStepData(lastdays);

            for(int i = 0;i<lastdays;i++){

                //activityData
                long actMin = (long)ad.get(i).getValue();
                barEntriesExercise.add(new BarEntry(i, Float.valueOf(actMin)));


                //SleepData
                Date temp_start = (Date)sd.get(i).getStartDate();
                Date temp_end   = (Date)sd.get(i).getEndDate();
                long sleep_min = (long)sd.get(i).getValue();
                barEntriesSleep.add(new BarEntry(i, Float.valueOf(sleep_min)));

                //Sleep circle moon
                setSleepCycleView(temp_start, temp_end, startSleep_2, endSleep_2, proBar_2);
                if( i==0){
                    setSleepCycleView(temp_start,temp_end,startSleep_2,endSleep_2,proBar_2);
                    textViewSleep_2.setText(getTimeFromMin(sleep_min));
                }

                //TODO mood data



            }

            long actMin2 = (long)ad.get(0).getValue();
            String actMinStr = Long.toString(actMin2);
            textViewExercise_2.setText(actMinStr + " min");

            Collections.reverse(barEntriesSleep);
            Collections.reverse(barEntriesExercise);



        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }


        //BarDataSets
        BarDataSet barDataSetExercise = new BarDataSet(barEntriesExercise, "Exercise");
        barDataSetExercise.setColors(Color.rgb(242, 162, 162));

        BarDataSet barDataSetSleep = new BarDataSet(barEntriesSleep, "Sleep");
        barDataSetSleep.setColors(Color.rgb(136, 139, 221));


        barDataSetSleep.setValueFormatter(new SleepValueFormatter());
        barDataSetExercise.setValueFormatter(new SleepValueFormatter());

        barDataSetExercise.setValueTextSize(14f);
        barDataSetSleep.setValueTextSize(14f);

        BarData data = new BarData(barDataSetExercise, barDataSetSleep);

        float barSpace = 0.16f;
        float barWidth = 0.15f;
        float groupSpace = calcGroupSpace(2, barSpace, barWidth);

        barChart_2.setData(data);
        data.setBarWidth(barWidth);
        barChart_2.groupBars(1, groupSpace, barSpace);

        //X-axis
        XAxis xAxis = barChart_2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        barChart_2.getXAxis().setGranularityEnabled(true);
        xAxis.setDrawGridLines(true);
        xAxis.setAxisMinimum(1);
        xAxis.setAxisMaximum(32);
        xAxis.setValueFormatter(new dateFormatter());
        xAxis.setTextSize(16f);
        barChart_2.setExtraOffsets(0,0,20,12);
        xAxis.setCenterAxisLabels(true);

        //Y-axis
        YAxis yAxisLeft = barChart_2.getAxisLeft();
        YAxis yAxisRight = barChart_2.getAxisRight();
        yAxisLeft.setEnabled(false);
        yAxisRight.setEnabled(false);
        yAxisLeft.setDrawGridLines(false);

        //Scrolling
        barChart_2.setDragEnabled(true);
        barChart_2.setVisibleXRangeMaximum(5);
        barChart_2.moveViewToX(31);
        barChart_2.invalidate();
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

            String pattern = "d";
            int val= Math.round(value);
            cal.add(Calendar.DATE, val - 31);
            if(val - 31 == 0){
                pattern = "d/M";
            }
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            String date = dateFormat.format(cal.getTime());

            return date;
        }
    }

    private String getTime(Date in){
        if(in!= null){
            String formattedDate = new SimpleDateFormat("HH:mm").format(in);
            return formattedDate;
        }
        else{
            return "00:00";
        }
    }

    private int calcProgress(Date start,Date end){
        long diff = end.getTime() - start.getTime();
        return (int)diff/(12*60*60*10);
    }

    private int calcStartAngleProgressBar(Date start){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int startOfProgressBar = -90;
        int angle = ((60*hours+minutes)/2)+startOfProgressBar;
        return angle;
    }

    private void setSleepCycleView(Date start,Date end,TextView startText,TextView endText,ProgressBar probar) {
        if ((start != null) && (end != null)) {
            startText.setText(getTime(start));
            endText.setText(getTime(end));
            probar.setProgress(calcProgress(start, end));
            probar.setRotation(calcStartAngleProgressBar(start));
        } else {
            startText.setText("");
            endText.setText("");
            probar.setProgress(0);
            probar.setRotation(0);
        }
    }

    private String getTimeFromMin(long min){
        long diffHours =min/ 60;
        long diffMinutes = min%60;

        return diffHours + "h "+ diffMinutes +"m";
    }

    private float calcGroupSpace(float n, float barSpace, float barWidth){
        float groupSpace = 1 - (barSpace + barWidth) * n;

        return groupSpace;
    }


}
