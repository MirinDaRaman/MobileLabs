package com.example.android.mobilecourse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextView mainText;
    TextView signOutButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mainText = (TextView) findViewById(R.id.main_text);
        signOutButton = (TextView) findViewById(R.id.link_signout);

        FirebaseUser userCredentials = mAuth.getCurrentUser();
        if (userCredentials != null) {
            mainText.setText(userCredentials.getDisplayName());
            mainText.setText(getString(R.string.Welcome) + " " + userCredentials.getDisplayName());

            signOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAuth.getCurrentUser() != null) {
                        mAuth.signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                }
            });
        }
    }
}