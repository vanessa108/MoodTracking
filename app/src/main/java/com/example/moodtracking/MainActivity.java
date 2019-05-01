package com.example.moodtracking;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    public static Long[] allTimeValues;
    public static Long[] thirtyDayValues;
    public static List<extData> sd;
    public static List<extData> md;
    public static List<extData> ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new HomeFragment());


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

         allTimeValues = graphData(40);
         thirtyDayValues = graphData(30);
         ArrayList<List<extData>> allData = loadData(31);
         sd = allData.get(0);
         md = allData.get(1);
         ad = allData.get(2);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                if(!MoodSelectedFragment.moodSelected) {

                    fragment = new HomeFragment();
                    break;
                }else if (MoodSelectedFragment.moodSelected){
                    fragment = new MoodSelectedFragment();
                    break;
                }

            case R.id.navigation_calendar:
                fragment = new CalendarFragment();
                break;

            case R.id.navigation_stats:
                fragment = new StatsBarChartFragment();
                break;

            case R.id.navigation_settings:
                fragment = new SettingsFragment();
                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public ArrayList<List<extData>> loadData(int numDays) {
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

        ArrayList<List<extData>> data = new ArrayList<List<extData>>();
        data.add(sd); data.add(md); data.add(ad);

        return data;

    }

    public Long[] graphData (int numDays) {

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
            long etemp = (long) ad.get(i).getValue();

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

        Long[] values = {s_supsad, s_sad, s_neutral, s_happy, s_suphappy, e_supsad, e_sad, e_neutral,
                e_happy, e_suphappy, ctr_supsad, ctr_sad, ctr_neutral, ctr_happy, ctr_suphappy, max_exercise, max_sleep};

        return values;

    }


}


