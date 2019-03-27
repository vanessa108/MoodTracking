package com.example.moodtracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class Calendar extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_mood:
                    Intent intent1 = new Intent(Calendar.this, MainActivity.class);
                    startActivity(intent1);
                    return true;
                case R.id.navigation_calendar:
//                    Intent intent2 = new Intent(Calendar.this, Calendar.class);
//                    startActivity(intent2);
                    return true;
                case R.id.navigation_stats:
                    Intent intent3 = new Intent(Calendar.this, Stats.class);
                    startActivity(intent3);
                    return true;
                case R.id.navigation_settings:
                    Intent intent4 = new Intent(Calendar.this, Settings.class);
                    startActivity(intent4);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
