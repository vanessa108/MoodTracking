package com.example.moodtracking;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.res.TypedArrayUtils;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;


public class BackgroundComp extends Activity{


    private String url="http://www.google.co.in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        new AsyncCaller().execute();

    }

    public class AsyncCaller extends AsyncTask<ArrayList<Long[]>, Void, ArrayList<Long[]>>
    {

        ProgressDialog pdLoading = new ProgressDialog(BackgroundComp.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }
        @Override
        protected ArrayList<Long[]> doInBackground(ArrayList<Long[]>... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            Long thirtydays[] = graphData(30);
            Long alltime[] = graphData(40);
            ArrayList<Long[]> combined = new ArrayList<Long[]>();
            combined.add(thirtydays);
            combined.add(alltime);
            // :(

            return combined;

        }


        @Override
        protected void onPostExecute(ArrayList<Long[]> result) {
            super.onPostExecute(result);
            //this method will be running    on UI thread

            pdLoading.dismiss();
        }

    }

    public  Long[] graphData(int numDays) {

        InputStream exp = getResources().openRawResource(R.raw.export);
        InputStream mod = getResources().openRawResource(R.raw.mooddata);
        HealthData hd = new HealthData(exp,mod);
        List<extData> sd = null;
        List<extData> md = null;
        List<extData> ad = null;

        try{
            sd = hd.getSleepData(numDays);
            md = hd.getMoodData(numDays);
            ad = hd.getStepData(numDays);

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }


//        int mood_day = 0;
        long s_supsad = 0;
        long s_sad = 0;
        long s_neutral = 0;
        long s_happy = 0;
        long s_suphappy = 0;

        long e_supsad = 0;
        long e_sad = 0;
        long e_neutral = 0;
        long e_happy = 0;
        long e_suphappy = 0;

        long ctr_supsad = 0;
        long ctr_sad = 0;
        long ctr_neutral = 0;
        long ctr_happy = 0;
        long ctr_suphappy = 0;
        long max_sleep = 0;
        long max_exercise = 0;



        for (int i = 0; i < md.size(); i++) {
            int mood = (int) md.get(i).getValue();
            long stemp = (long) sd.get(i).getValue();
            long etemp = (long) sd.get(i).getValue();

            if (stemp > max_sleep) {
                max_sleep = stemp;
            }
            if (etemp > max_exercise) {
                max_exercise = etemp;
            }
            if (mood == 1) {
                s_supsad += stemp;
                e_supsad += etemp;
                ctr_supsad++;
            }
            else if (mood == 2) {
                s_sad += stemp;
                e_sad += etemp;
                ctr_sad++;
            } else if (mood == 3) {
                s_neutral += stemp;
                e_neutral += etemp;
                ctr_neutral++;
            } else if (mood == 4) {
                s_happy += stemp;
                e_happy += etemp;
                ctr_happy++;
            } else if (mood == 5) {
                s_suphappy += stemp;
                e_suphappy += etemp;
                ctr_suphappy++;
            }

        }

        Long[] results = {s_supsad, s_sad, s_neutral, s_happy, s_suphappy, e_supsad, e_sad, e_neutral,
                e_happy, e_suphappy, ctr_supsad, ctr_sad, ctr_neutral, ctr_happy, ctr_suphappy};

        return results;

    }
}
