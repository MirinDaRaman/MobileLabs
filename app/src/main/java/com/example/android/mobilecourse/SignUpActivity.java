package com.example.android.mobilecourse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.annotation.NonNull;

public class SignUpActivity extends Activity {
    private FirebaseAuth mAuth;
    DatabaseReference databaseUsers;
    private EditText emailText;
    private TextInputLayout nameTextContainer;
    private TextInputLayout inputEmailContainer;
    private TextInputLayout inputPasswordContainer;
    private TextInputLayout phoneTextContainer;
    private EditText nameText;
    private EditText passwordText;
    private EditText phoneNumber;
    private Button signupButton;
    private TextView loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        inputEmailContainer = findViewById(R.id.input_email_container);
        emailText = findViewById(R.id.input_email);
        nameTextContainer = findViewById(R.id.input_name_container);
        inputPasswordContainer = findViewById(R.id.input_password_container);
        phoneTextContainer = findViewById(R.id.input_phone_container);
        nameText = findViewById(R.id.input_name);
        passwordText = findViewById(R.id.input_password);
        phoneNumber = findViewById(R.id.input_phone);
        signupButton = findViewById(R.id.btn_signup);
        loginLink = findViewById(R.id.link_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }
        signupButton.setEnabled(false);

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String phone = phoneNumber.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            UserCustomFields UserCustomFields = new UserCustomFields(phone,email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(UserCustomFields).addOnCompleteListener(task1 -> {
                                signupButton.setEnabled(true);
                                addUsername(name);

                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            onSignupFailed();
                        }
                    }
                });
    }

    private void addUsername(String userName){
        FirebaseUser userProfile = mAuth.getCurrentUser();
        UserProfileChangeRequest userUpdateProfile = new UserProfileChangeRequest
                .Builder().setDisplayName(userName).build();
        userProfile.updateProfile(userUpdateProfile)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task1) {
                        if(task1.isSuccessful()){
                            Toast.makeText(getBaseContext(), getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finish();
                        }else {
                            Toast.makeText(getBaseContext(),getString(R.string.database_failure), Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    private void onSignupFailed() {
        Toast.makeText(getBaseContext(), getString(R.string.registration_failed), Toast.LENGTH_LONG).show();
        signupButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        String password = passwordText.getText().toString();
        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String phone = phoneNumber.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameTextContainer.setError(getText(R.string.invalid_name));
            valid = false;
        } else {
            nameTextContainer.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmailContainer.setError(getText(R.string.invalid_email));
            valid = false;
        } else {
            emailText.setError(null);
        }
        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
            phoneTextContainer.setError(getText(R.string.invalid_phone));
            valid = false;
        } else {
            phoneTextContainer.setError(null);
        }
        if (password.isEmpty() || password.length() < 8) {
            inputPasswordContainer.setError(getText(R.string.invalid_password));
            valid = false;
        } else {
            passwordText.setError(null);
        }
        return valid;
    }
}
