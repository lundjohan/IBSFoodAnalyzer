package com.ibsanalyzer.inputday;

import android.os.Bundle;

public class ExerciseActivity extends EventActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_exercise;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
    }
}
