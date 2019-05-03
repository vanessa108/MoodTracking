package com.example.moodtracking;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Calendar;


public class StatsBarChartFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_stats_bar_chart, container, false);

        final TextView date_2 = (TextView) relativeLayout.findViewById(R.id.date_2);
        final TextView textViewSleep_2 = (TextView) relativeLayout.findViewById(R.id.textViewSleep_2);
        final TextView tv_month = (TextView) relativeLayout.findViewById(R.id.textViewMonth);
        final BarChart barChart_2 = (BarChart) relativeLayout.findViewById(R.id.barchart_2);
        final ImageView mood = (ImageView) relativeLayout.findViewById(R.id.selectedMoodIMG_2);
        final TextView mood_text = (TextView) relativeLayout.findViewById(R.id.textViewMood_2);
        final TextView textViewExercise_2 = (TextView) relativeLayout.findViewById(R.id.textViewExercise_2);
        Button pie_chart_button = (Button) relativeLayout.findViewById(R.id.pieChartButton);

        final TextView startSleep_2 = (TextView) relativeLayout.findViewById(R.id.StartBedTime_2);
        final TextView endSleep_2 = (TextView) relativeLayout.findViewById(R.id.EndBedTime_2);
        final ProgressBar proBar_2 = (ProgressBar) relativeLayout.findViewById(R.id.sleepCircle_2);

        String date = new SimpleDateFormat("EEE d MMM yyyy", Locale.getDefault()).format(new Date());
        date_2.setText("Today " + date);

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





        final ArrayList<BarEntry> barEntriesSleep = new ArrayList<>();
        final ArrayList<BarEntry> barEntriesExercise = new ArrayList<>();
        final ArrayList<Integer> mood_data = new ArrayList<>();

        List<extData> sd = MainActivity.sd;
        List<extData> md = MainActivity.md;
        List<extData> ad = MainActivity.ad;
        /*Read data*/
        InputStream exp = getResources().openRawResource(R.raw.export);
        InputStream mod = getResources().openRawResource(R.raw.mooddata);

        final List<Date> startDates = new ArrayList<>();
        final List<Date> endDates = new ArrayList<>();

        for(int i = 0;i<31;i++) {

            //activityData
            long actMin = (long) ad.get(i).getValue();
            barEntriesExercise.add(new BarEntry(i, Float.valueOf(actMin)));


            //SleepData
            Date temp_start = sd.get(i).getStartDate();
            Date temp_end = sd.get(i).getEndDate();
            long sleep_min = (long) sd.get(i).getValue();
            barEntriesSleep.add(new BarEntry(i, Float.valueOf(sleep_min)));
            startDates.add(temp_start);
            endDates.add(temp_end);

            //Sleep circle moon
            setSleepCycleView(temp_start, temp_end, startSleep_2, endSleep_2, proBar_2);
            if (i == 0) {
                setSleepCycleView(temp_start, temp_end, startSleep_2, endSleep_2, proBar_2);
                textViewSleep_2.setText(getTimeFromMin(sleep_min));
            }

            //MoodData
           mood_data.add((int)md.get(i).getValue());
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());





        //Set init values for the first view
        //Mood
        final int todayMood =         preferences.getInt("todayMood",999);
        setMoodView(todayMood, mood, mood_text);
        //Sleep
        textViewSleep_2.setText(getTimeFromMin((long)barEntriesSleep.get(0).getY()));
        setSleepCycleView(startDates.get(0),endDates.get(0),startSleep_2,endSleep_2,proBar_2);
        //exercise
        long actMin2 = (long)ad.get(0).getValue();
        textViewExercise_2.setText(getTimeFromMin(actMin2));


        Collections.reverse(barEntriesSleep);
        Collections.reverse(barEntriesExercise);
        Collections.reverse(startDates);
        Collections.reverse(endDates);
        Collections.reverse((mood_data));


        //BarDataSets
        final BarDataSet barDataSetExercise = new BarDataSet(barEntriesExercise, "Exercise");



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
        xAxis.setGranularityEnabled(true);
        xAxis.setAxisMinimum(-1);
        xAxis.setAxisMaximum(34);
        xAxis.setValueFormatter(new dateFormatter());
        xAxis.setTextSize(18f);
        barChart_2.setExtraOffsets(0,0,20,12);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawAxisLine(false);

        //Y-axis
        YAxis yAxisLeft = barChart_2.getAxisLeft();
        YAxis yAxisRight = barChart_2.getAxisRight();
        yAxisLeft.setEnabled(false);
        yAxisRight.setEnabled(false);
        yAxisLeft.setDrawGridLines(false);


        xAxis.setDrawGridLines(true);



        //Scrolling
        barChart_2.setDragEnabled(true);
        barChart_2.setVisibleXRangeMaximum(5);
        barChart_2.moveViewToX(34);
        barChart_2.invalidate();

        //int chart_width = barChart_2.get;
        //int chart_pos = chart_width;
        final ViewPortHandler handler = barChart_2.getViewPortHandler();
        barChart_2.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {}
            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

                MPPointD bottomLeft = barChart_2.getValuesByTouchPoint(handler.contentLeft(), handler.contentBottom(), YAxis.AxisDependency.LEFT);
                int moveToVal = (int)(bottomLeft.x);
                barChart_2.moveViewToX((float)(moveToVal));
                //Date and mood
                Date date = endDates.get(moveToVal+1);
                if (DateUtils.isToday(date.getTime())){
                //if(date.equals(today)){
                    date_2.setText("Today " + setDateString(endDates.get(moveToVal+1)));
                    setMoodView(todayMood, mood, mood_text);
                }else{
                    date_2.setText(setDateString(date));
                    setMoodView((mood_data.get(moveToVal + 1)), mood, mood_text);
                }
                //Exercise
                textViewExercise_2.setText(getTimeFromMin((long)barEntriesExercise.get(moveToVal+1).getY()));
                //Sleep
                textViewSleep_2.setText(getTimeFromMin((long)barEntriesSleep.get(moveToVal+1).getY()));
                setSleepCycleView(startDates.get(moveToVal+1),endDates.get(moveToVal+1),startSleep_2,endSleep_2,proBar_2);

                //Month
                String month = new SimpleDateFormat("MMMM YYYY").format(endDates.get(moveToVal+1));
                tv_month.setText(month);
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {
            }
            @Override
            public void onChartDoubleTapped(MotionEvent me) {
            }
            @Override
            public void onChartSingleTapped(MotionEvent me) {
            }
            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
            }
            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
            }
            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
            }
        });

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

            int val= Math.round(value);
            cal.add(Calendar.DATE, val - 31);
            String str = Integer.toString(val - 31);

            String pattern = "d";
            DateFormat dateFormat = new SimpleDateFormat(pattern);

            String date = dateFormat.format(cal.getTime());

            if(date.equals("1")){
                String pattern2 = "d/M";
                DateFormat dateFormat2 = new SimpleDateFormat(pattern2);

                date = dateFormat2.format(cal.getTime());
            }

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
        if (min>0) {
            long diffHours = min / 60;
            long diffMinutes = min % 60;
            return diffHours + "h " + diffMinutes + "m";
        }
        else{
            return "no data";
        }
    }

    private float calcGroupSpace(float n, float barSpace, float barWidth){
        float groupSpace = 1 - (barSpace + barWidth) * n;

        return groupSpace;
    }
    private String setDateString(Date in){
        if(in!= null){
            String formattedDate = new SimpleDateFormat("EEE d MMM yyyy").format(in);
            return formattedDate;
        }
        else{
            return "no Data";
        }
    }
    private void setMoodView(int mood,ImageView iv,TextView tv){
        if (mood == 1) {
            iv.setImageResource(R.drawable.supersad);
            tv.setText("Awful");
        } else if (mood == 2) {
            iv.setImageResource(R.drawable.sad);
            tv.setText("Rough");
        } else if (mood == 3) {
            iv.setImageResource(R.drawable.neutral);
            tv.setText("Ok");
        } else if (mood == 4) {
            iv.setImageResource(R.drawable.happy);
            tv.setText("Good");
        } else if (mood == 5) {
            iv.setImageResource(R.drawable.superhappy);
            tv.setText("Awesome");
        } else if (mood ==999){
            iv.setImageResource(R.drawable.no_data);
            tv.setText("");

        }
    }


}
