package com.moutimid.familywellness.user.home.Reminder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moutamid.familywellness.R;

public class MainActivity extends AppCompatActivity {
//    list of users:
    public RecyclerView recyclerView;
//    database object:
    public MedicalDB DbHelper;
    public UserListAdapter userlistAdapter;
    public FloatingActionButton add_user_fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_alarm);
        //Database instantiation:
        DbHelper = MedicalDB.getInstance(this.getApplicationContext());


        //Main activity for showing the list of users
        //Initialize recycler view:

        Cursor user_list = DbHelper.getUserList(DbHelper.getWritableDatabase());


        recyclerView = (RecyclerView) findViewById(R.id.user_list);
        add_user_fab = (FloatingActionButton) findViewById(R.id.add_user);

        //Initialize layout manager (default = vertical) and set to the recycler view:
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        //Instantiate object of recycler view by its adapter:
        userlistAdapter = new UserListAdapter(MainActivity.this,DbHelper);
        userlistAdapter.setUserData(DbHelper.getUserList(DbHelper.getWritableDatabase()));
        recyclerView.setAdapter(userlistAdapter);

        add_user_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTextDialog().show();

            }
        });



    }

    private AlertDialog myTextDialog() {
        View layout = View.inflate(this, R.layout.add_user_dialog, null);
        EditText savedText = ((EditText) layout.findViewById(R.id.add_username));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DbHelper.addUser(DbHelper.getWritableDatabase(),savedText.getText().toString().trim());
                Cursor user_list = DbHelper.getUserList(DbHelper.getWritableDatabase());
                userlistAdapter.setUserData(user_list);
                userlistAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(userlistAdapter);

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setView(layout);
        return builder.create();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userlistAdapter.setUserData(DbHelper.getUserList(DbHelper.getWritableDatabase()));
        userlistAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(userlistAdapter);
    }

    public void back(View view) {
        onBackPressed();
    }
}