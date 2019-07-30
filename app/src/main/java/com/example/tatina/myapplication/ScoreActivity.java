package com.example.tatina.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;

public class ScoreActivity extends Activity {


    TextView total_score_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        String total_score = getIntent().getStringExtra("total_score");
        System.out.println(total_score);


        total_score_textView = findViewById(R.id.total_score_textView);
        total_score_textView.setText(total_score);

    }

    void exit(View w){
         finish();
    }
}
