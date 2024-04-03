package com.example.labusageaccountability.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.labusageaccountability.DatabaseHelper.DatabaseHelper;
import com.example.labusageaccountability.R;

import java.util.List;

public class ManagerActivity extends AppCompatActivity {
    SearchView searchView;
    ListView listView;
    ArrayAdapter<String> adapter;
    List<String> clientActivities;
    Button logOutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        searchView = findViewById(R.id.searchBar);
        listView = findViewById(R.id.listView);
        logOutButton = findViewById(R.id.managerLogOut);

        try {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            clientActivities = dbHelper.getAllClientsWithActivity();
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, clientActivities);
            listView.setAdapter(adapter);
            Log.i("Client Activities", clientActivities.toString());
            dbHelper.close();
        } catch (Exception e) {
            Log.e("ManagerActivity", "Error accessing database: " + e.getMessage());
        }

        // Search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}