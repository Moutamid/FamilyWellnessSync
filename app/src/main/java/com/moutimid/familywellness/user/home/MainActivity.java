package com.moutimid.familywellness.user.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.familywellness.R;
import com.moutimid.familywellness.user.home.Chatgpt.AskGPT;
import com.moutimid.familywellness.user.home.Pharmacy.AllCategoriesActivity;
import com.moutimid.familywellness.user.home.Pharmacy.CartActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout CustomCartContainer;
    private TextView PageTitle;
    private TextView CustomCartNumber;

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
                startActivity(new Intent(MainActivity.this, com.moutimid.familywellness.user.home.BMI.MainActivity.class));
            }
        });
        findViewById(R.id.pharmacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AllCategoriesActivity.class));
            }
        });
        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });
        findViewById(R.id.chatbot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AskGPT.class));
            }
        });
        findViewById(R.id.schedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, com.moutimid.familywellness.user.home.Reminder.MainActivity.class));
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

    private void setNumberOfItemsInCartIcon() {
        DatabaseReference root = FirebaseDatabase.getInstance("https://childfr-35a43-default-rtdb.firebaseio.com/").getReference().child("FamilyWillness");
        DatabaseReference m = root.child("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.getChildrenCount() == 1) {
                        CustomCartNumber.setVisibility(View.GONE);
                    } else {
                        CustomCartNumber.setVisibility(View.VISIBLE);
                        CustomCartNumber.setText(String.valueOf(dataSnapshot.getChildrenCount() - 1));
                    }
                } else {
                    CustomCartNumber.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }


    private void HandleTotalPriceToZeroIfNotExist() {
        DatabaseReference root = FirebaseDatabase.getInstance("https://childfr-35a43-default-rtdb.firebaseio.com/").getReference().child("FamilyWillness");
        DatabaseReference m = root.child("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    FirebaseDatabase.getInstance("https://childfr-35a43-default-rtdb.firebaseio.com/").getReference().child("FamilyWillness").child("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("totalPrice").setValue("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

    }


    @Override
    protected void onResume() {
        super.onResume();
        showCartIcon();
        HandleTotalPriceToZeroIfNotExist();
    }

    private void showCartIcon() {
        CustomCartContainer = (RelativeLayout) findViewById(R.id.CustomCartIconContainer);
        CustomCartNumber = (TextView) findViewById(R.id.CustomCartNumber);
        setNumberOfItemsInCartIcon();
        CustomCartContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });
    }

}
