package com.moutimid.familywellness.user.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.moutamid.familywellness.R;
import com.moutimid.familywellness.authetications.AccountActivity;
import com.moutimid.familywellness.authetications.LoginActivity;
import com.moutimid.familywellness.user.home.Order.OrderActivity;

public class ProfileActivity extends AppCompatActivity {
    TextView name_txt, name_latter, textView7;
    RelativeLayout delete_data, privacy, order, profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name_txt = findViewById(R.id.textView6);
        profile = findViewById(R.id.profile);
        delete_data = findViewById(R.id.delete_data);
        privacy = findViewById(R.id.privacy);
        order = findViewById(R.id.order);
        name_latter = findViewById(R.id.textView5);
        name_txt.setText(Stash.getString("name"));
        char c = Stash.getString("name").charAt(0);
        name_latter.setText(c + "");
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, OrderActivity.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(ProfileActivity.this, AccountActivity.class));
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, OrderActivity.class));
            }
        });
        delete_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finishAffinity();
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName() + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

    }

    public void backPress(View view) {
        onBackPressed();
    }
}