package com.riteshkm.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference root;
    TextView userName;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        auth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance().getReference("Users");

        userName = findViewById(R.id.user_detail);

        if (auth.getCurrentUser() != null) {

            String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.getValue(User.class).getName();
                    String email = snapshot.getValue(User.class).getEmail();

                    progressBar.setVisibility(View.GONE);
                    userName.setText(name);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    public void logOut(View view) {
        auth.signOut();
        Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Profile.this, Login.class));
        finish();
    }
}