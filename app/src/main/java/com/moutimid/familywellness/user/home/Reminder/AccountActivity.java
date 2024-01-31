package com.moutimid.familywellness.user.home.Reminder;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.moutamid.familywellness.R;
import com.moutimid.familywellness.authetications.LoginActivity;

import java.util.Objects;


public class AccountActivity extends AppCompatActivity {
    public static final String TAG = AccountActivity.class.getSimpleName();
    private Button signOut;
    private EditText name, newEmail;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        signOut = (Button) findViewById(R.id.sign_out);

        newEmail = (EditText) findViewById(R.id.new_email);
        name = (EditText) findViewById(R.id.name);
        name.setText(Stash.getString("name"));
        newEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newEmail.getText().toString().isEmpty() || name.getText().toString().isEmpty()) {
                    Toast.makeText(AccountActivity.this, "Please enter name /email", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Dialog lodingbar = new Dialog(AccountActivity.this);
                    lodingbar.setContentView(R.layout.loading);
                    Objects.requireNonNull(lodingbar.getWindow()).setBackgroundDrawable(new ColorDrawable(UCharacter.JoiningType.TRANSPARENT));
                    lodingbar.setCancelable(false);
                    lodingbar.show();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(FirebaseAuth.getInstance().getCurrentUser().getEmail(), Stash.getString("password")); // Current Login Credentials \\
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {


                                                           FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                           user.updateEmail(newEmail.getText().toString())
                                                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                       @Override
                                                                       public void onComplete(@NonNull Task<Void> task) {
                                                                           if (task.isSuccessful()) {
                                                                               FirebaseDatabase.getInstance().getReference().child("FamilyWillness").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(name.getText().toString());
                                                                               FirebaseDatabase.getInstance().getReference().child("FamilyWillness").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").setValue(newEmail.getText().toString());
                                                                               lodingbar.dismiss();
                                                                               finish();
                                                                               Stash.put("name", name.getText().toString());
                                                                           } else {
                                                                               FirebaseDatabase.getInstance().getReference().child("FamilyWillness").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(name.getText().toString());
                                                                               lodingbar.dismiss();
                                                                               finish();
                                                                               Stash.put("name", name.getText().toString());
                                                                               Toast.makeText(AccountActivity.this, "Email can't be changed", Toast.LENGTH_SHORT).show();
                                                                           }
                                                                       }
                                                                   }).addOnFailureListener(new OnFailureListener() {
                                                                       @Override
                                                                       public void onFailure(@NonNull Exception e) {
                                                                           FirebaseDatabase.getInstance().getReference().child("FamilyWillness").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(name.getText().toString());
                                                                           lodingbar.dismiss();
                                                                           finish();
                                                                           Stash.put("name", name.getText().toString());
                                                                           Toast.makeText(AccountActivity.this, "Email can't be changed", Toast.LENGTH_SHORT).show();

                                                                       }
                                                                   });
                                                       }
                                                   }
                            );
                }
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    public void backPress(View view) {
        onBackPressed();
    }
}