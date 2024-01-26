package com.moutimid.familywellness.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.moutamid.familywellness.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkApp(MainActivity.this);
        findViewById(R.id.emergency).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, EmergencyCallActivity.class));
            }
        });
 findViewById(R.id.bmi_calculator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, com.moutimid.familywellness.home.BMI.MainActivity.class));
            }
        });

    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId()== R.id.action_setting)
//        {
//            Intent settings = new Intent(this, AccountActivity.class);
//            startActivity(settings);
//        }
//        return super.onOptionsItemSelected(item);
//    }
public static void checkApp(Activity activity) {
    String appName = "Family Wellness";

    new Thread(() -> {
        URL google = null;
        try {
            google = new URL("https://raw.githubusercontent.com/Moutamid/Moutamid/main/apps.txt");
        } catch (final MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
        } catch (final IOException e) {
            e.printStackTrace();
        }
        String input = null;
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if ((input = in != null ? in.readLine() : null) == null) break;
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            stringBuffer.append(input);
        }
        try {
            if (in != null) {
                in.close();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        String htmlData = stringBuffer.toString();

        try {
            JSONObject myAppObject = new JSONObject(htmlData).getJSONObject(appName);

            boolean value = myAppObject.getBoolean("value");
            String msg = myAppObject.getString("msg");

            if (value) {
                activity.runOnUiThread(() -> {
                    new AlertDialog.Builder(activity)
                            .setMessage(msg)
                            .setCancelable(false)
                            .show();
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }).start();
}
}
