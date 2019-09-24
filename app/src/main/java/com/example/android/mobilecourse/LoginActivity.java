package com.example.android.mobilecourse;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private FirebaseAuth mAuth;

    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Start the Signup activity
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void login() {

        if (!validate()) {
            Toast.makeText(LoginActivity.this, "Please validate your email and password", Toast.LENGTH_LONG).show();
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);


        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            onLoginSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            onLoginFailed();
                        }

                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    public void onLoginSuccess() {
        Log.d("My_tag", "signInWithEmail:success");
        FirebaseUser user = mAuth.getCurrentUser();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        Toast.makeText(getBaseContext(), "Oh! You are so crappy today, success", Toast.LENGTH_LONG).show();
        finish();
        _loginButton.setEnabled(true);
    }

    public void onLoginFailed() {
        Log.w("My_tag", "signInWithEmail:failure");
        Toast.makeText(LoginActivity.this, "Authentication failed.",
                Toast.LENGTH_SHORT).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 8) {
            _passwordText.setError("between 8+ alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}