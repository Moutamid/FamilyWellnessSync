package com.moutimid.familywellness.user.home.Pharmacy;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.familywellness.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private TextView mPerson_name;
    private CircleImageView mPerson_image;
    private RelativeLayout CustomCartContainer;
    private TextView PageTitle;
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        fa = this;

//        mToolbar = (Toolbar)findViewById(R.id.cartToolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle("My Cart");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.Cartframe, new MyCartFragment()).commit();

    }


    @Override
    protected void onStart() {
        super.onStart();
//        drawerLayout = (DrawerLayout) findViewById(R.id.cartDrawer);
//        navigationView = (NavigationView) findViewById(R.id.cartNavigationViewer);
//
//        //navigation header
//        navigationView.setNavigationItemSelectedListener(this);
//        View view = navigationView.getHeaderView(0);
//        mPerson_name = view.findViewById(R.id.persname);
//        mPerson_image = view.findViewById(R.id.circimage);
//
//        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
//        drawerLayout.addDrawerListener(mToggle);
//        mToggle.syncState();

//        getNavHeaderData();

        showCartIcon();
    }


    private void getNavHeaderData() {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        String CurrentUser = mAuth.getCurrentUser().getUid();
        DatabaseReference root = FirebaseDatabase.getInstance("https://childfr-35a43-default-rtdb.firebaseio.com/").getReference().child("FamilyWillness");
        DatabaseReference m = root.child("users").child(CurrentUser);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String Name = snapshot.child("Name").getValue().toString();
                    String Image = snapshot.child("Image").getValue().toString();
                    mPerson_name.setText(Name);
                    if (Image.equals("default")) {
                        Picasso.get().load(R.drawable.profile).into(mPerson_image);
                    } else
                        Picasso.get().load(Image).placeholder(R.drawable.profile).into(mPerson_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }


    private void showCartIcon() {
        //toolbar & cartIcon


        //************custom action items xml**********************
        CustomCartContainer = (RelativeLayout) findViewById(R.id.CustomCartIconContainer);
        PageTitle = (TextView) findViewById(R.id.PageTitle);
        PageTitle.setVisibility(View.GONE);
        CustomCartContainer.setVisibility(View.GONE);

    }

    public void backPress(View view) {
        onBackPressed();
    }
}