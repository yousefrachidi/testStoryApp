package com.example.applicationpfe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.applicationpfe.module.ChatGPTAPI;
import com.example.applicationpfe.module.PasswordHashing;
import com.example.applicationpfe.module.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView signupRedirectText;

    FirebaseFirestore db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initiaView();

    }

    private void initiaView() {
         db = FirebaseFirestore.getInstance();

        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);

        loginButton.setOnClickListener(view -> {

            if (!validateUsername() | !validatePassword()) {
                Toast.makeText(LoginActivity.this, "Your username or password in valid", Toast.LENGTH_SHORT).show();

            } else {
                checkUser();
            }
        });

        signupRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

    }

    public Boolean validateUsername() {
        String val = loginUsername.getText().toString();
        if (val.isEmpty()) {
            loginUsername.setError("Username cannot be empty");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser(){

        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();
         userPassword = PasswordHashing.hashPassword(userPassword);


        db.collection("users")
                .whereEqualTo("username", userUsername)
                .whereEqualTo("password", userPassword)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // User with matching credentials found
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            User user = documentSnapshot.toObject(User.class);
                            // Do something with the user data
                            Log.d("TAG", "User found: " + user.getName());

                            onNext(user);
                            loginUsername.setError(null);
                        }
                    } else {
                        // No user found with the provided credentials
                        Log.d("TAG", "User not found");
                        loginPassword.setError("Invalid Credentials");
                        loginPassword.requestFocus();
                    }
                })
                .addOnFailureListener(e -> {
                    // Error retrieving user data
                    Log.w("TAG", "Error getting user document", e);
                });

    }

    private void onNext(User user) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        intent.putExtra("name", user.getName());
        intent.putExtra("email", user.getEmail());
        intent.putExtra("username", user.getUsername());
        intent.putExtra("password", user.getPassword());

        startActivity(intent);
    }





}