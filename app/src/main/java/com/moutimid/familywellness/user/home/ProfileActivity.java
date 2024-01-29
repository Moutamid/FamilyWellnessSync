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
import com.moutimid.familywellness.authetications.LoginActivity;

public class ProfileActivity extends AppCompatActivity {
    TextView name_txt, name_latter, textView7;
    RelativeLayout delete_data, privacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name_txt = findViewById(R.id.textView6);
        delete_data = findViewById(R.id.delete_data);
        privacy = findViewById(R.id.privacy);
        name_latter = findViewById(R.id.textView5);
        name_txt.setText(Stash.getString("name"));
        char c = Stash.getString("name").charAt(0);
        name_latter.setText(c + "");
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
                startActivity(new Intent(ProfileActivity.this, WebViewActivity.class));
            }
        });

    }

    public void backPress(View view) {
        onBackPressed();
    }
}