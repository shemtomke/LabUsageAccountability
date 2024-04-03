package com.example.labusageaccountability.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.labusageaccountability.DatabaseHelper.DatabaseHelper;
import com.example.labusageaccountability.Models.Staff;
import com.example.labusageaccountability.R;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextUsername, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editUsernameAdmin);
        editTextPassword = findViewById(R.id.editPasswordAdmin);

        Button buttonLogin = findViewById(R.id.loginButton);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }
    private void login() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Staff staff = new Staff(username, password);
        boolean isValid = dbHelper.login(staff);

        if (isValid) {
            // Retrieve the role of the staff member
            String role = dbHelper.getRole(username);

            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
            Log.d("MyTag", "Role " + role);
            // Redirect to the appropriate activity based on the user's role
            if ("admin".equals(role)) {
                Intent intent = new Intent(this, AdminActivity.class);
                startActivity(intent);
            } else if ("staff".equals(role)) {
                Intent intent = new Intent(this, StaffActivity.class);
                startActivity(intent);
            } else if ("manager".equals(role)) {
                Intent intent = new Intent(this, ManagerActivity.class);
                startActivity(intent);
            }
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
        dbHelper.close();
    }
}