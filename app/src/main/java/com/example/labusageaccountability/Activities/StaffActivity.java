package com.example.labusageaccountability.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.labusageaccountability.DatabaseHelper.DatabaseHelper;
import com.example.labusageaccountability.Models.ActivityRecord;
import com.example.labusageaccountability.R;

public class StaffActivity extends AppCompatActivity {
    EditText clientNameText, purposeText, startTimeText, endTimeText;
    Button recordClientButton, logOutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        clientNameText = findViewById(R.id.editClientName);
        purposeText = findViewById(R.id.editPurpose);
        startTimeText = findViewById(R.id.editStartTime);
        endTimeText = findViewById(R.id.editEndTime);

        recordClientButton = findViewById(R.id.recordClientButton);
        logOutButton = findViewById(R.id.staffLogOutButton);

        recordClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordClients();
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void RecordClients()
    {
        String clientName = clientNameText.getText().toString();
        String purpose = purposeText.getText().toString();
        String startTime = startTimeText.getText().toString();
        String endTime = endTimeText.getText().toString();

        if (!clientName.isEmpty() && !purpose.isEmpty() && !startTime.isEmpty() && !endTime.isEmpty()) {
            ActivityRecord activityRecord = new ActivityRecord(clientName, purpose, startTime, endTime);
            long result = new DatabaseHelper(this).addActivity(activityRecord);
            if (result != -1) {
                // Record added successfully
                Toast.makeText(this, "Recorded Client Successfully!", Toast.LENGTH_SHORT).show();
                // Clear input fields
                clientNameText.setText("");
                purposeText.setText("");
                startTimeText.setText("");
                endTimeText.setText("");
            } else {
                // Failed to add record
                // Handle error
                Toast.makeText(this, "Failed to record Client Activity!", Toast.LENGTH_SHORT).show();
            }
        } else {
            // All fields are required
            // Show error message or toast
            Toast.makeText(this, "Failed to record Client Activity! All fields are required!", Toast.LENGTH_SHORT).show();
        }
    }
}