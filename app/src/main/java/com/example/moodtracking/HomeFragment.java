package com.example.moodtracking;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class HomeFragment extends Fragment {
    static int moodSelected;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_home, container, false);

        ImageButton supersad = (ImageButton) relativeLayout.findViewById(R.id.supersadButton);
        ImageButton sad = (ImageButton) relativeLayout.findViewById(R.id.sadButton);
        ImageButton neutral = (ImageButton) relativeLayout.findViewById(R.id.neutralButton);
        ImageButton happy = (ImageButton) relativeLayout.findViewById(R.id.happyButton);
        ImageButton superhappy = (ImageButton) relativeLayout.findViewById(R.id.superhappyButton);

        ImageButton info = (ImageButton) relativeLayout.findViewById(R.id.infoButton);



        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new MoodInfoFragment();
                replaceFragment(newFragment);
            }
        });


        supersad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodSelected = 1;
                Fragment newFragment = new MoodSelectedFragment();
                replaceFragment(newFragment);
            }
        });

        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodSelected = 2;
                Fragment newFragment = new MoodSelectedFragment();
                replaceFragment(newFragment);
            }
        });

        neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodSelected = 3;
                Fragment newFragment = new MoodSelectedFragment();
                replaceFragment(newFragment);
            }
        });

        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodSelected = 4;
                Fragment newFragment = new MoodSelectedFragment();
                replaceFragment(newFragment);
            }
        });

        superhappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moodSelected = 5;
                Fragment newFragment = new MoodSelectedFragment();
                replaceFragment(newFragment);
            }
        });






        return relativeLayout;

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MoodSelectedFragment.setParameters(moodSelected);
    }
}
