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

    private float rotation = 0F;
    Bitmap supsadExBMP;
    int supsadExBmpWidth, supsadExBmpHeight, arrow_x, arrow_y;
    ImageView supsadExercise, exerciseChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_stats, container, false);

        supsadExercise = (ImageView) relativeLayout.findViewById(R.id.supsadExercise);
        supsadExBMP =  BitmapFactory.decodeResource(getContext().getResources(), R.drawable.arrow);
        supsadExBmpHeight = supsadExBMP.getHeight();
        supsadExBmpWidth = supsadExBMP.getWidth();

        exerciseChart = (ImageView) relativeLayout.findViewById(R.id.activity_semicircle);
//
        arrow_x = exerciseChart.getLeft() + (exerciseChart.getWidth())/2;
        arrow_y = supsadExercise.getBottom();


        drawMatrix();


        return relativeLayout;
    }

    private void drawMatrix() {

//        Matrix matrix = new Matrix();
//        matrix.setRotate(rotation,1000, 900);
//        Bitmap targetBitmap = Bitmap.createBitmap(supsadExBMP, 0, 0, supsadExBmpWidth, supsadExBmpHeight, matrix, true);
//        supsadExercise.setImageBitmap(targetBitmap);




//        matrix.postRotate(rotation);
//        Bitmap newBM = Bitmap.createBitmap(supsadExBMP, 0, 0, supsadExBMP.getWidth(), supsadExBMP.getHeight(), matrix, true);
//        supsadExercise.setImageBitmap(newBM );

//        Bitmap targetBitmap = Bitmap.createBitmap(supsadExBMP.getWidth(), supsadExBmpHeight, supsadExBMP.getConfig());
//        Canvas canvas = new Canvas(targetBitmap);
//        Matrix matrix = new Matrix();
//        matrix.setRotate(rotation, supsadExercise.getWidth() / 2, supsadExercise.getHeight() /2);
//        canvas.drawBitmap(supsadExBMP, matrix, new Paint());
//        supsadExercise.setImageBitmap(targetBitmap);

//
//        Matrix matrix = new Matrix();
//        supsadExercise.setScaleType(ImageView.ScaleType.MATRIX);
//        matrix.postRotate(rotation, supsadExercise.getDrawable().getBounds().width() / 2,
//                supsadExercise.getDrawable().getBounds().height() / 2);
//        supsadExercise.setImageMatrix(matrix);

        RotateAnimation rotate = new RotateAnimation(0.0f, rotation, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 1.0f);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setDuration(0);
        rotate.setFillAfter(true);
        supsadExercise.startAnimation(rotate);






//        Matrix matrixb = new Matrix();
//        Matrix matrixc = new Matrix();
//        matrixb.setRotate(rotation,100, supsadExBmpHeight);
//        matrixc.setTranslate(100, 0);
//        matrix.setConcat(matrixb, matrixc);
//        Bitmap targetBitmap = Bitmap.createBitmap(supsadExBmpWidth, supsadExBmpHeight, supsadExBMP.getConfig());
//        Canvas canvas = new Canvas(targetBitmap);
//        canvas.drawBitmap(supsadExBMP, matrix, new Paint());
//        supsadExercise.setImageBitmap(targetBitmap);




        //matrix.postRotate(rotation);

        //matrix.setRotate(rotation);
        //matrix.setRotate(rotation, supsadExBmpWidth/2, supsadExBmpHeight/2);
//        matrix.setRotate(rotation, 100, bmpHeight);
        //matrix.setTranslate(100, 0);
//        matrix.setConcat(matrixb, matrixc);



//
//        Matrix matrix = new Matrix();

//        RectF rectF = new RectF(0, 0, supsadExercise.getWidth(), supsadExercise.getHeight());
//        matrix.mapRect(rectF);
//
//        Bitmap targetBitmap = Bitmap.createBitmap(supsadExercise.getWidth(), supsadExercise.getHeight(), supsadExBMP.getConfig());
//        Canvas canvas = new Canvas(targetBitmap);
//        canvas.drawBitmap(supsadExBMP, matrix, new Paint());

//        Canvas canvas = new Canvas(targetBitmap);
//        canvas.drawBitmap(bitmap, matrix, new Paint());
//        arrowImage.setImageBitmap(targetBitmap);

//
        //Bitmap rotatedBitmap = Bitmap.createBitmap(supsadExBMP, 0, 0, supsadExBmpWidth, supsadExBmpHeight, matrix, true);
//        supsadExercise.setImageBitmap(rotatedBitmap);

//        supsadExercise.setRotation(rotation);
//        supsadExercise.setX(supsadExercise.getWidth() / 2 - 200);
//        supsadExercise.setY(supsadExercise.getHeight());



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
