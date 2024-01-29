package com.moutimid.familywellness.user.home.BMI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.moutamid.familywellness.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void onBackPress(View view) {
        onBackPressed();
    }

    public void back(View view) {
        onBackPressed();
    }
}