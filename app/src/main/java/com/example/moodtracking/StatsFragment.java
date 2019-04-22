package com.example.moodtracking;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class StatsFragment extends Fragment {

    private float rotation = 90F;
    Bitmap supsadExBMP;
    int supsadExBmpWidth, supsadExBmpHeight;
    ImageView supsadExercise;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_stats, container, false);

        supsadExercise = (ImageView) relativeLayout.findViewById(R.id.supsadExercise);
        supsadExBMP =  BitmapFactory.decodeResource(getContext().getResources(), R.drawable.arrow);
        supsadExBmpHeight = supsadExBMP.getHeight();
        supsadExBmpWidth = supsadExBMP.getWidth();


        drawMatrix();


        return relativeLayout;
    }

    private void drawMatrix() {
        Matrix matrix = new Matrix();
//        Matrix matrixb = new Matrix();
//        Matrix matrixc = new Matrix();

//        matrix.postRotate(rotation);
        matrix.setRotate(rotation, supsadExBmpWidth/2, supsadExBmpHeight/2);
//        matrix.setRotate(rotation, 100, bmpHeight);
//        matrix.setTranslate(100, 0);
//        matrix.setConcat(matrixb, matrixc);

//        Bitmap targetBitmap = Bitmap.createBitmap(100, bmpHeight, bitmap.getConfig());
//        Canvas canvas = new Canvas(targetBitmap);
//        canvas.drawBitmap(bitmap, matrix, new Paint());
//        arrowImage.setImageBitmap(targetBitmap);

//
        Bitmap rotatedBitmap = Bitmap.createBitmap(supsadExBMP, 0, 0, supsadExBmpWidth, supsadExBmpHeight, matrix, true);
        supsadExercise.setImageBitmap(rotatedBitmap);

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
