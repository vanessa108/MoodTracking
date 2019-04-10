package com.example.moodtracking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MoodSelectedFragment extends Fragment {

    static int selectedMood;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_mood_selected, container, false);

        ImageView whichMood = (ImageView) relativeLayout.findViewById(R.id.selectedMoodIMG);
        TextView dateText = (TextView) relativeLayout.findViewById(R.id.date);
        ProgressBar proBar = (ProgressBar) relativeLayout.findViewById(R.id.sleepCircle);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        dateText.setText(date);

        if (selectedMood == 1) {
            whichMood.setImageResource(R.drawable.supersad);
        } else if (selectedMood == 2) {
            whichMood.setImageResource(R.drawable.sad);
        } else if (selectedMood == 3) {
            whichMood.setImageResource(R.drawable.neutral);
        } else if (selectedMood == 4) {
            whichMood.setImageResource(R.drawable.happy);
        } else if (selectedMood == 5) {
            whichMood.setImageResource(R.drawable.superhappy);
        }

        proBar.setSecondaryProgress(80);
        return relativeLayout;

    }


    public static void setParameters(int mood) {
        MoodSelectedFragment.selectedMood = mood;
    }
}
