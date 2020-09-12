package com.riteshkm.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class Register extends AppCompatActivity {
    private EditText userName, inputEmail, inputPassword, confirmPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        userName = (EditText) findViewById(R.id.userName);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void loginPage(View view) {
        startActivity(new Intent(Register.this, Login.class));
        finish();
    }

    public void SignUp(View view) {

        final String username = userName.getText().toString().trim();
        final String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String confirm_password = confirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            userName.setError("Enter your name!");
            userName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            inputEmail.setError("Enter email address!");
            inputEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Email not Valid...!");
            inputEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Enter password!");
            inputPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            inputPassword.setError("Password too short, enter minimum 6 characters!");
            inputPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(confirm_password)) {
            confirmPassword.setError("Enter Confirm password!");
            confirmPassword.requestFocus();
            return;
        }
        if (!confirm_password.equals(password)) {
            confirmPassword.setError("Confirm Password is not same...!");
            confirmPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this, "Authentication failed - " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                } else {

                    User user = new User(username, email);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Register.this, "User Created Successfully", Toast.LENGTH_SHORT).show();

                                Toast.makeText(Register.this, "Moving to the Profile Page in 5 seconds", Toast.LENGTH_SHORT).show();

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(new Intent(Register.this, Profile.class));
                                        finish();
                                    }
                                }, 5 * 1000);
                            }
                        }
                    });
                }
            }
        });


    }
}