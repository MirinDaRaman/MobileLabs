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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginActivity extends Activity {
    private FirebaseAuth mAuth;

    @BindView(R.id.input_email)
    EditText emailText;
    @BindView(R.id.input_password)
    EditText passwordText;
    @BindView(R.id.btn_login)
    Button loginButton;
    @BindView(R.id.link_signup)
    TextView signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void login() {

        if (!validate()) {
            Toast.makeText(LoginActivity.this, getText(R.string.validate_email_password), Toast.LENGTH_LONG).show();
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

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

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError(getText(R.string.invalid_email));
            valid = false;
        } else {
            emailText.setError(null);
        }
        if (password.isEmpty() || password.length() < 8) {
            passwordText.setError(getText(R.string.invalid_password));
            valid = false;
        } else {
            passwordText.setError(null);
        }
        return valid;
    }
}