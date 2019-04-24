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
import com.github.mikephil.charting.data.Entry;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Calendar;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;


public class MoodSelectedFragment extends Fragment {

    static int selectedMood;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_mood_selected, container, false);

        ImageView whichMood = (ImageView) relativeLayout.findViewById(R.id.selectedMoodIMG);
        ImageView whichMood1 = (ImageView) relativeLayout.findViewById(R.id.selectedMoodIMG1);
        ImageButton editPen = (ImageButton) relativeLayout.findViewById(R.id.editPenButton);
        TextView whichMoodText = (TextView) relativeLayout.findViewById(R.id.textViewMood);
        TextView dateText = (TextView) relativeLayout.findViewById(R.id.date);
        TextView dateText2 = (TextView) relativeLayout.findViewById(R.id.today);
        ProgressBar proBar = (ProgressBar) relativeLayout.findViewById(R.id.sleepCircle);
        BarChart barChart = (BarChart) relativeLayout.findViewById(R.id.barchart);
        TextView startSleep = (TextView) relativeLayout.findViewById(R.id.StartBedTime);
        TextView endSleep = (TextView) relativeLayout.findViewById(R.id.EndBedTime);
        TextView sleepText = (TextView) relativeLayout.findViewById(R.id.textViewSleep);
        TextView threeDaysAgoText = (TextView) relativeLayout.findViewById(R.id.textViewThreeDaysAgo);
        TextView twoDaysAgoText = (TextView) relativeLayout.findViewById(R.id.textViewTwoDaysAgo);
        TextView yesterdayText = (TextView) relativeLayout.findViewById(R.id.textViewYesterday);

        String date = new SimpleDateFormat("EEE dd/MM", Locale.getDefault()).format(new Date());
        dateText.setText(date);

        if (selectedMood == 1) {
            whichMood.setImageResource(R.drawable.supersad);
            whichMoodText.setText("Awful");
            moodSelected = true;
            whichMood1.setImageResource(R.drawable.supersad);
        } else if (selectedMood == 2) {
            whichMood.setImageResource(R.drawable.sad);
            whichMood1.setImageResource(R.drawable.sad);
            moodSelected = true;
            whichMoodText.setText("Rough");
        } else if (selectedMood == 3) {
            whichMood.setImageResource(R.drawable.neutral);
            moodSelected = true;
            whichMood1.setImageResource(R.drawable.neutral);
            whichMoodText.setText("Ok");
        } else if (selectedMood == 4) {
            whichMood.setImageResource(R.drawable.happy);
            moodSelected = true;
            whichMood1.setImageResource(R.drawable.happy);
            whichMoodText.setText("Good");
        } else if (selectedMood == 5) {
            whichMood.setImageResource(R.drawable.superhappy);
            whichMood1.setImageResource(R.drawable.superhappy);
            moodSelected = true;
            whichMoodText.setText("Awesome");
        }

        //Create function to calculate the start angle and the progress based on the time
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String date1 = "2019/04/09 24:00:00";
        String date2 = "2019/04/10 8:00:00";
        Date start = null;
        Date end = null;
        try {
            start = format.parse(date1);
            end = format.parse(date2);

        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        Set sleep progress bar values
        */
        startSleep.setText(getTime(start));
        endSleep.setText(getTime(end));
        proBar.setSecondaryProgress(calcProgress(start,end));
        proBar.setRotation(calcStartAngleProgressBar(start));
        sleepText.setText(getSleepTime(start,end));


        editPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new HomeFragment();
                replaceFragment(newFragment);
            }
        });

        //Bar Chart
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(600);
        barChart.setPinchZoom(false);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        ArrayList<BarEntry> barEntriesExercise = new ArrayList<>();
        barEntriesExercise.add(new BarEntry(0, 40f));
        barEntriesExercise.add(new BarEntry(1, 30f));
        barEntriesExercise.add(new BarEntry(2, 20f));
        barEntriesExercise.add(new BarEntry(3, 46f));

        ArrayList<BarEntry> barEntriesSleep = new ArrayList<>();

        String todaySleep;
        String yesterdaySleep;
        String twoDaysAgoSleep;
        String threeDaysAgoSleep;
        float todaySleepMins;
        float yesterdaySleepMins;
        float twoDaysAgoSleepMins;
        float threeDaysAgoSleepMins;

        InputStream exp = getResources().openRawResource(R.raw.export);
        InputStream mod = getResources().openRawResource(R.raw.mooddata);
        HealthData hd = new HealthData(exp,mod);
        List<extData> sd = null;

        //TODO avoid casting from int to float and back
        try {
            sd = hd.getSleepData(4);
            long temp1 = (long)sd.get(0).getValue();
            long temp2 = (long)sd.get(1).getValue();
            long temp3 = (long)sd.get(2).getValue();
            long temp4 = (long)sd.get(3).getValue();

            todaySleepMins = (float)temp1;
            yesterdaySleepMins = (float)temp2;
            twoDaysAgoSleepMins = (float)temp3;
            threeDaysAgoSleepMins = (float)temp4;

            barEntriesSleep.add(new BarEntry(0, threeDaysAgoSleepMins));
            barEntriesSleep.add(new BarEntry(1, twoDaysAgoSleepMins));
            barEntriesSleep.add(new BarEntry(2, yesterdaySleepMins));
            barEntriesSleep.add(new BarEntry(3, todaySleepMins));

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

        barDataSetExercise.setValueTextSize(14f);
        barDataSetSleep.setValueTextSize(14f);

        BarData data = new BarData(barDataSetExercise, barDataSetSleep);



        float groupSpace = 0.5f;
        float barSpace = 0.13f;
        float barWidth = 0.12f;

        barChart.setData(data);
        data.setBarWidth(barWidth);
        barChart.groupBars(1, groupSpace, barSpace);

        //X-axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //TODO fix axis spacing & values above bars to 4 h 30 m..
        xAxis.setGranularity(1);
        barChart.getXAxis().setGranularityEnabled(true);
        xAxis.setEnabled(false);
        xAxis.setAxisMinimum(1f);
        xAxis.setAxisMaximum(5);


        //Y-axis
        YAxis yAxisLeft = barChart.getAxisLeft();
        YAxis yAxisRight = barChart.getAxisRight();
        yAxisLeft.setEnabled(false);
        yAxisRight.setEnabled(false);
        yAxisLeft.setDrawGridLines(false);

        barChart.invalidate();

        String today = new SimpleDateFormat("dd/MM", Locale.getDefault()).format(new Date());
        dateText2.setText("Today " + today);

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("EEE dd/MM");
        cal.add(Calendar.DATE, -1);
        yesterdayText.setText(dateFormat.format(cal.getTime()));

        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DATE, - 2);
        twoDaysAgoText.setText(dateFormat.format(cal2.getTime()));

        Calendar cal3 = Calendar.getInstance();
        cal3.add(Calendar.DATE, - 3);
        threeDaysAgoText.setText(dateFormat.format(cal3.getTime()));

        return relativeLayout;
    }

    public static void setParameters(int mood) {
        MoodSelectedFragment.selectedMood = mood;
    }

    public static Boolean moodSelected = false;

    private void replaceFragment (Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
    private int calcStartAngleProgressBar(Date start){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int startOfProgressBar = 90;
        int angle = ((60*hours+minutes)/2)-startOfProgressBar;
        return angle;
    }
    private int calcProgress(Date start,Date end){
        long diff = end.getTime() - start.getTime();
        /** remove the milliseconds part */
        diff = diff / 1000;
        long hours = diff / (60 * 60) % 24;
        long percentage = hours/12;
        return (int)percentage;
    }
    private String getSleepTime(Date start,Date end){
        long diff = end.getTime() - start.getTime();
        /** remove the milliseconds part */
        diff = diff / 1000;
        long diffMinutes = diff / (60 ) % 60;
        long diffHours = diff / (60 * 60 );
        return Long.toString(diffHours)+"h "+Long.toString(diffMinutes)+"m";

    }

    private String getTime(Date in){
        String formattedDate = new SimpleDateFormat("HH:mm").format(in);
        return formattedDate;
    }

    private String nodeToString(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            System.out.println("nodeToString Transformer Exception");
        }
        return sw.toString();
    }

    public float calcMinutes(String hoursMins){

        String hourString = hoursMins.split("h")[0];
        float hours = Float.parseFloat(hourString);

        String minsString = hoursMins.split("m")[0];
        minsString = minsString.substring(Math.max(minsString.length() - 2, 0));
        float mins = Float.parseFloat(minsString);

        float totalMins = hours * 60f + mins;

        return totalMins;
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

}
