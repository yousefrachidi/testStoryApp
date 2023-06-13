package com.example.applicationpfe;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.applicationpfe.module.PasswordHashing;
import com.example.applicationpfe.module.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupUsername, signupEmail, signupPassword;
    TextView loginRedirectText;
    Button signupButton;


    // Get a reference to the Firebase Realtime Database
    FirebaseFirestore db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initiateView();
    }

    private void initiateView() {

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);

        db = FirebaseFirestore.getInstance();

        signupButton.setOnClickListener(view -> {
            register();
        });

        loginRedirectText.setOnClickListener(view -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });

    }

    private void register() {

        // Create a new User instance
            String name = signupName.getText().toString().trim();
            String email = signupEmail.getText().toString().trim();
            String username = signupUsername.getText().toString().trim();
            String password = signupPassword.getText().toString().trim();


        if (!name.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty() ) {

            password =  PasswordHashing.hashPassword(password);
            User user = new User(name, email, username, password);
            saveUser(user);

        }else if (name.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Entre your Name", Toast.LENGTH_SHORT).show();
        }else if (email.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Entre your Email", Toast.LENGTH_SHORT).show();
        }else if (username.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Entre your Username", Toast.LENGTH_SHORT).show();
        }else if (password.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Entre your Password", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveUser( User user) {
        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(SignupActivity.this, "You have signup successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    // Error writing document
                    Toast.makeText(SignupActivity.this, "Try again please !", Toast.LENGTH_SHORT).show();
                });
    }
}