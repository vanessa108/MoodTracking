package com.example.moodtracking;

;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
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

public class StatsFragment extends Fragment{

    ImageView supsadExercise, sadExercise, neutralExercise, happyExercise,suphappyExercise;
    ImageView supsadSleep, sadSleep, neutralSleep, happySleep, suphappySleep;
    Button barChartButton;
    Button allTimeData, ThirtyDaysData;
    TextView maxExercise, maxSleep;
    ImageView infoBtn;

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
        infoBtn = (ImageView) relativeLayout.findViewById(R.id.info_btn);

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
                allTimeData.setBackgroundResource(R.drawable.clicked_button);
                allTimeData.setTextColor(Color.WHITE);
                ThirtyDaysData.setBackgroundResource(R.drawable.clickable_button);
                ThirtyDaysData.setTextColor(Color.parseColor("#008577"));

                updateGraph(MainActivity.allTimeValues);


            }
        });

        ThirtyDaysData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThirtyDaysData.setBackgroundResource(R.drawable.clicked_button);
                ThirtyDaysData.setTextColor(Color.WHITE);
                allTimeData.setBackgroundResource(R.drawable.clickable_button);
                allTimeData.setTextColor(Color.parseColor("#008577"));
                updateGraph(MainActivity.thirtyDayValues);

            }
        });

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new StatsInfoFragment();
                replaceFragment(newFragment);
            }
        });


        updateGraph(MainActivity.thirtyDayValues);





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


    public void updateGraph (Long[] values) {

        long s_supsad, s_sad, s_neutral, s_happy, s_suphappy, e_supsad, e_sad, e_neutral, e_happy, e_suphappy, ctr_supsad,
                ctr_sad, ctr_neutral, ctr_happy, ctr_suphappy, max_exercise, max_sleep;
        s_supsad = values[0]; s_sad = values[1]; s_neutral = values[2]; s_happy = values[3]; s_suphappy = values[4];
        e_supsad = values[5]; e_sad = values[6]; e_neutral = values[7]; e_happy = values[8]; e_suphappy = values[9];
        ctr_supsad = values[10]; ctr_sad = values[11]; ctr_neutral = values[12]; ctr_happy = values[13]; ctr_suphappy = values[14];
        max_exercise = values[15]; max_sleep = values[16];

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

        maxExercise.setText(Integer.toString(maxExerciseH) + "h " + Integer.toString(maxExerciseM) + "m");

        int maxSleepH = (int) max_sleep / 60;
        int maxSleepM = (int) (max_sleep % 60 );

        maxSleep.setText(Integer.toString(maxSleepH) + "h " + Integer.toString(maxSleepM) + "m");


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
