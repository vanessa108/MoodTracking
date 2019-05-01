package com.example.moodtracking;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import static java.lang.Math.floor;
import static java.lang.Math.max;

public class StatsFragment extends Fragment {

    private float rotation = 45F;
    int numDays = 30;
    ImageView supsadExercise, sadExercise, neutralExercise, happyExercise,suphappyExercise;
    ImageView supsadSleep, sadSleep, neutralSleep, happySleep, suphappySleep;
    Button barChartButton;
    Button allTimeData, ThirtyDaysData;
    TextView maxExercise, maxSleep;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_stats, container, false);

        supsadExercise = (ImageView) relativeLayout.findViewById(R.id.supsadExercise);
        sadExercise = (ImageView) relativeLayout.findViewById(R.id.sadExercise);
        neutralExercise = (ImageView) relativeLayout.findViewById(R.id.neutralExercise);
        happyExercise = (ImageView) relativeLayout.findViewById(R.id.happyExercise);
        suphappyExercise = (ImageView) relativeLayout.findViewById(R.id.suphappyExercise);

        supsadSleep = (ImageView) relativeLayout.findViewById(R.id.supsadSleep);
        sadSleep = (ImageView) relativeLayout.findViewById(R.id.sadSleep);
        neutralSleep = (ImageView) relativeLayout.findViewById(R.id.neutralSleep);
        happySleep = (ImageView) relativeLayout.findViewById(R.id.happySleep);
        suphappySleep = (ImageView) relativeLayout.findViewById(R.id.suphappySleep);

        maxExercise = (TextView) relativeLayout.findViewById(R.id.maxExercise);
        maxSleep = (TextView) relativeLayout.findViewById(R.id.maxSleep);
        barChartButton = (Button) relativeLayout.findViewById(R.id.barChartButton);
        allTimeData = (Button) relativeLayout.findViewById(R.id.stats_alltime);
        ThirtyDaysData = (Button) relativeLayout.findViewById(R.id.stats_30days);

        barChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new StatsBarChartFragment();
                replaceFragment(newFragment);
            }
        });

        allTimeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numDays = 100;
                allTimeData.setBackgroundResource(R.drawable.clicked_button);
                allTimeData.setTextColor(Color.WHITE);
                ThirtyDaysData.setBackgroundResource(R.drawable.clickable_button);
                ThirtyDaysData.setTextColor(Color.parseColor("#008577"));


            }
        });

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

        int ctr_supsad = 0;
        int ctr_sad = 0;
        int ctr_neutral = 0;
        int ctr_happy = 0;
        int ctr_suphappy = 0;
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


        float s_supsadP = (float) (s_supsad/ctr_supsad) / max_sleep * 180 - 90;
        float s_sadP = (float) (s_sad/ctr_sad) / max_sleep * 180 - 90;
        float s_neutralP = (float) (s_neutral/ctr_neutral) / max_sleep * 180 - 90;
        float s_happyP = (float) (s_happy/ctr_happy) / max_sleep * 180 - 90;
        float s_suphappyP = (float) (s_suphappy/ctr_suphappy) / max_sleep * 180 - 90;

        float e_supsadP = (float) (e_supsad/ctr_supsad) / max_exercise * 180 - 90;
        float e_sadP = (float) (e_sad/ctr_sad) / max_exercise * 180 - 90;
        float e_neutralP = (float) (e_neutral/ctr_neutral) / max_exercise * 180 - 90;
        float e_happyP = (float) (e_happy/ctr_happy) / max_exercise * 180 - 90;
        float e_suphappyP = (float) (e_suphappy/ ctr_suphappy) / max_exercise * 180 - 90;

        if (s_supsad <= 0) {
            supsadSleep.setVisibility(View.GONE);
        }
        if (s_sad <= 0) {
            sadSleep.setVisibility(View.GONE);
        }
        if (s_neutral <= 0) {
            neutralSleep.setVisibility(View.GONE);
        }
        if (s_happy <= 0) {
            happySleep.setVisibility(View.GONE);
        }
        if (s_suphappy <= 0) {
            supsadSleep.setVisibility(View.GONE);
        }

        if (e_supsad <= 0) {
            supsadExercise.setVisibility(View.GONE);
        }
        if (e_sad <= 0) {
            sadExercise.setVisibility(View.GONE);
        }
        if (e_neutral <= 0) {
            neutralExercise.setVisibility(View.GONE);
        }
        if (e_happy <= 0) {
            happyExercise.setVisibility(View.GONE);
        }
        if (e_suphappy <= 0) {
            suphappyExercise.setVisibility(View.GONE);
        }

        int maxExerciseH = (int) max_exercise / 60;
        int maxExerciseM = (int) (max_exercise % 60);

        maxExercise.setText(Integer.toString(maxExerciseH) + "h" + Integer.toString(maxExerciseM) + "m");

        int maxSleepH = (int) max_sleep / 60;
        int maxSleepM = (int) (max_sleep % 60 );

        maxSleep.setText(Integer.toString(maxSleepH) + "h" + Integer.toString(maxSleepM) + "m");


        rotateArrow(e_supsadP, supsadExercise);
        rotateArrow(e_sadP, sadExercise);
        rotateArrow(e_neutralP, neutralExercise);
        rotateArrow(e_happyP, happyExercise);
        rotateArrow(e_suphappyP, suphappyExercise);

        rotateArrow(s_supsadP, supsadSleep);
        rotateArrow(s_sadP, sadSleep);
        rotateArrow(s_neutralP, neutralSleep);
        rotateArrow(s_happyP, happySleep);
        rotateArrow(s_suphappyP, suphappySleep);


        return relativeLayout;
    }

    private void rotateArrow(float rotation, ImageView emoji) {

        RotateAnimation rotate = new RotateAnimation(0.0f, rotation, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 1.0f);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setDuration(0);
        rotate.setFillAfter(true);
        emoji.startAnimation(rotate);

    }

    private float calculatePercentage(long mood, int ctr, long max_val) {
        return (float) (mood/ctr) / max_val * 180 - 90;
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

}
