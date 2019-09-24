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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends Activity {
    private FirebaseAuth mAuth;
    DatabaseReference databaseUsers;

    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_phone) EditText _phoneNumber;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
       _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!= null){
//            user already has logined in
        }
    }

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }
        _signupButton.setEnabled(false);

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String phone = _phoneNumber.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            UserCustomFields UserCustomFields = new UserCustomFields(name, email, phone);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(UserCustomFields).addOnCompleteListener(task1 -> {
                                _signupButton.setEnabled(true);
                                        if(task1.isSuccessful()){
                                            Toast.makeText(getBaseContext(), getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                            finish();
                                        }else {
                                            Toast.makeText(getBaseContext(),getString(R.string.database_failure), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            onSignupFailed();
                        }
                    }
                });
    }

    private void onSignupFailed() {
        Toast.makeText(getBaseContext(), getString(R.string.registration_failed), Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String phone = _phoneNumber.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError(getText(R.string.invalid_name));
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(getText(R.string.invalid_email));
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (email.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
            _phoneNumber.setError(getText(R.string.invalid_phone));
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 8) {
            _passwordText.setError(getText(R.string.invalid_password));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
