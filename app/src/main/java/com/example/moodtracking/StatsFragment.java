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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class StatsFragment extends Fragment {

    private float rotation = 45F;
    Bitmap supsadExBMP;
    int supsadExBmpWidth, supsadExBmpHeight, arrow_x, arrow_y;
    ImageView supsadExercise, sadExercise, neutralExercise, happyExercise,suphappyExercise;
    ImageView supsadSleep, sadSleep, neutralSleep, happySleep, suphappySleep;

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




        rotateArrow(-42f, supsadExercise);
        rotateArrow(28, sadExercise);
        rotateArrow(-19, neutralExercise);
        rotateArrow(-35, happyExercise);
        rotateArrow(85, suphappyExercise);

        rotateArrow(20f, supsadSleep);
        rotateArrow(10f, sadSleep);
        rotateArrow(-2f, neutralSleep);
        rotateArrow(-40f, happySleep);
        rotateArrow(89f, suphappySleep);




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









    //
//        PieChart exerciseChart = (PieChart) relativeLayout.findViewById(R.id.exerciseChart);
//
//
//        List<PieEntry> entries = new ArrayList<>();
//        entries.add(new PieEntry(18.5f, "Super sad"));
//        entries.add(new PieEntry(26.7f, "Sad"));
//        entries.add(new PieEntry(12f, "Neutral"));
//        entries.add(new PieEntry(12f, "Happy"));
//        entries.add(new PieEntry(12f, "Super Happy"));
//
//        PieDataSet set = new PieDataSet(entries, "Test");
//        PieData data = new PieData(set);
//        exerciseChart.setMaxAngle(180f);
//        exerciseChart.setRotation(270f);
//        set.setSliceSpace(5);
//        set.setColor(Color.MAGENTA);
//        exerciseChart.setData(data);
//        exerciseChart.invalidate();



}
