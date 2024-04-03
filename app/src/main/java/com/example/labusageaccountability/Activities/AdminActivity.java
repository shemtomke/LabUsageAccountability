package com.example.labusageaccountability.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.labusageaccountability.DatabaseHelper.DatabaseHelper;
import com.example.labusageaccountability.Models.Staff;
import com.example.labusageaccountability.R;

import java.util.List;

public class AdminActivity extends AppCompatActivity {
    Button logOutButton, addStaffButton, updateStaffButton, deleteStaffButton;
    Spinner roleSpinner;
    EditText staffUserNameText, staffPasswordText;
    ListView listView;
    List<String> staffDetails;
    ArrayAdapter<String> adapter;
    DatabaseHelper dbHelper;
    Staff selectedStaff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        addStaffButton = findViewById(R.id.addButtonAdmin);
        updateStaffButton = findViewById(R.id.updateButtonAdmin);
        deleteStaffButton = findViewById(R.id.deleteButtonAdmin);
        roleSpinner = findViewById(R.id.roleSpinner);
        staffPasswordText = findViewById(R.id.editPasswordAdmin);
        staffUserNameText = findViewById(R.id.editUsernameAdmin);
        listView = findViewById(R.id.staffListView);
        logOutButton = findViewById(R.id.adminLogOutButton);

        dbHelper = new DatabaseHelper(this);

        // Spinner Role Display: Manager, Admin, Staff
        RoleSpinner();

        // Generate a list of staff
        GetAllStaff();

        // When we click from the item list view
        GetStaffDetails();

        addStaffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateStaff();
            }
        });

        updateStaffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateStaff();
            }
        });

        deleteStaffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteStaff();
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    void GetAllStaff()
    {
        try {
            staffDetails = dbHelper.getAllStaffDetails();
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, staffDetails);
            listView.setAdapter(adapter);
            Log.i("Staff Details", staffDetails.toString());
            dbHelper.close();
        } catch (Exception e) {
            Log.e("ManagerActivity", "Error accessing database: " + e.getMessage());
        }
    }
    public void CreateStaff()
    {
        // Get the values from the EditText and Spinner
        String username = staffUserNameText.getText().toString().trim();
        String password = staffPasswordText.getText().toString().trim();
        String role = roleSpinner.getSelectedItem().toString();

        // Create a new Staff object and add it to the database
        Staff staff = new Staff(username, password, role);
        dbHelper.addStaff(staff);

        // Update the list view
        GetAllStaff();

        // Show a toast message
        Toast.makeText(this, "Staff created successfully", Toast.LENGTH_SHORT).show();

        // Clear the EditText fields
        staffUserNameText.setText("");
        staffPasswordText.setText("");
        dbHelper.close();
    }
    public void DeleteStaff()
    {
        if (selectedStaff != null) {
            // Delete the staff record from the database
            int rowsAffected = dbHelper.deleteStaff(selectedStaff.getId());

            // Update the list view
            GetAllStaff();

            // Show a toast message
            if (rowsAffected > 0) {
                Toast.makeText(this, "Staff deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to delete staff", Toast.LENGTH_SHORT).show();
            }

            // Clear the EditText fields
            staffUserNameText.setText("");
            staffPasswordText.setText("");
            dbHelper.close();
        } else {
            Toast.makeText(this, "Please select a staff member to delete", Toast.LENGTH_SHORT).show();
        }
    }
    public void UpdateStaff()
    {
        if (selectedStaff != null) {
            // Get the updated values from the EditText and Spinner
            String username = staffUserNameText.getText().toString().trim();
            String password = staffPasswordText.getText().toString().trim();
            String role = roleSpinner.getSelectedItem().toString();

            // Update the Staff object
            selectedStaff.setUsername(username);
            selectedStaff.setPassword(password);
            selectedStaff.setRole(role);

            // Update the staff record in the database
            int rowsAffected = dbHelper.updateStaff(selectedStaff);
            Log.i("STAFF ID", selectedStaff.toString() + ", " + selectedStaff.getId() + ", " + selectedStaff.getUsername());
            // Update the list view
            GetAllStaff();

            // Show a toast message
            if (rowsAffected > 0) {
                Toast.makeText(this, "Staff updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update staff", Toast.LENGTH_SHORT).show();
            }

            // Clear the EditText fields
            staffUserNameText.setText("");
            staffPasswordText.setText("");
            dbHelper.close();
        } else {
            Toast.makeText(this, "Please select a staff member to update", Toast.LENGTH_SHORT).show();
        }
    }
    public void GetStaffDetails()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String staffDetail = (String) parent.getItemAtPosition(position);
                String[] parts = staffDetail.split("\n");

                // Extracting username, role, and password from the selected item
                int staffId = Integer.parseInt(parts[0].substring(4).trim());
                String username = parts[1].substring(10).trim();
                String role = parts[3].substring(6).trim();
                String password = parts[2].substring(10).trim();

                selectedStaff = new Staff(staffId, username, password, role);

                // Setting the values to the EditText and Spinner
                staffUserNameText.setText(username);
                staffPasswordText.setText(password);
                roleSpinner.setSelection(getIndex(roleSpinner, role));
            }
        });
    }
    private int getIndex(Spinner spinner, String role) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(role)) {
                return i;
            }
        }
        return 0; // Default to the first item if role is not found
    }
    public void RoleSpinner()
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.roles,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedRole = (String) parentView.getItemAtPosition(position);
                // Handle the selected role
                Toast.makeText(AdminActivity.this, "Selected Role: " + selectedRole, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }
}