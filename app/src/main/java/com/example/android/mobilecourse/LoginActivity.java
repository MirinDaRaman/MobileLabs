package com.example.android.mobilecourse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;

public class LoginActivity extends Activity {
    private FirebaseAuth mAuth;
    private TextInputLayout inputEmailContainer;
    private EditText emailText;
    private EditText passwordText;
    private TextInputLayout inputPasswordContainer;
    private Button loginButton;
    private TextView signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmailContainer = findViewById(R.id.input_email_container);
        emailText = findViewById(R.id.input_email);
        passwordText = findViewById(R.id.input_password);
        inputPasswordContainer = findViewById(R.id.input_password_container);
        loginButton = findViewById(R.id.btn_login);
        signupLink = findViewById(R.id.link_signup);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(this, getText(R.string.login_success), Toast.LENGTH_LONG).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        loginButton.setOnClickListener(v -> login());
        signupLink.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void login() {
        if (!validate()) {
            Toast.makeText(LoginActivity.this, getText(R.string.validate_email_password), Toast.LENGTH_LONG).show();
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        onLoginSuccess();
                    } else {
                        // If sign in fails, display a message to the user.
                        onLoginFailed();
                    }
                });
    }

    private void onLoginSuccess() {
        FirebaseUser user = mAuth.getCurrentUser();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        Toast.makeText(getBaseContext(), getText(R.string.login_success), Toast.LENGTH_LONG).show();
        finish();
        loginButton.setEnabled(true);
    }

    private void onLoginFailed() {
        Toast.makeText(LoginActivity.this, getText(R.string.login_failed),
                Toast.LENGTH_SHORT).show();
        loginButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;
        String password = passwordText.getText().toString();
        String email = emailText.getText().toString();

        if (password.isEmpty() || password.length() < 8) {
            inputPasswordContainer.setError(getText(R.string.invalid_password));
            valid = false;
        } else {
            passwordText.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmailContainer.setError(getText(R.string.invalid_email));
            valid = false;
        } else {
            emailText.setError(null);
        }
        return valid;
    }
}