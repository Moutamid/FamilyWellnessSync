package com.moutimid.familywellness.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.moutamid.familywellness.R;

public class EmergencyCallActivity extends AppCompatActivity {
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_call);
        // Initialize the Handler
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(myRunnable, 6500);
        findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall("123");
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (handler != null) {
                    handler.removeCallbacks(myRunnable);
                    finish();
                }
            }
        });
    }

    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            makePhoneCall("123 ");
        }
    };

    public void back(View view) {
        onBackPressed();
    }

    private void makePhoneCall(String phoneNumber) {
        // Create an Intent to initiate a phone call
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));

        // Check if the app has permission to make a phone call
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the phone call
            startActivity(intent);
            if (handler != null) {
                handler.removeCallbacks(myRunnable);
                finish();
            }

        }
    }
}