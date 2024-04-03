package com.example.labusageaccountability.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.labusageaccountability.Models.ActivityRecord;
import com.example.labusageaccountability.Models.Staff;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lab_usage.db";
    private static final int DATABASE_VERSION = 1;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Table
        db.execSQL("CREATE TABLE staff (id INTEGER PRIMARY KEY, username TEXT, password TEXT, role TEXT)");
        db.execSQL("CREATE TABLE activities (id INTEGER PRIMARY KEY, client_name TEXT, purpose TEXT, start_time TEXT, end_time TEXT)");

        // Staff
        db.execSQL("INSERT INTO staff (username, password, role) VALUES ('admin', 'admin', 'admin')");
        db.execSQL("INSERT INTO staff (username, password, role) VALUES ('mike', '12345', 'staff')");
        db.execSQL("INSERT INTO staff (username, password, role) VALUES ('joshua', '12345', 'manager')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    // Login
    public boolean login(Staff staff)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("staff", null, "username=? AND password=?", new String[]{staff.getUsername(), staff.getPassword()}, null, null, null);
        boolean isValid = cursor.moveToFirst();
        cursor.close();
        db.close();
        return isValid;
    }
    public String getRole(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String role = null;

        try {
            Cursor cursor = db.query("staff", new String[]{"role"}, "username=?", new String[]{username}, null, null, null);

            if (cursor.moveToFirst()) {
                int roleIndex = cursor.getColumnIndex("role");
                if (roleIndex != -1) {
                    role = cursor.getString(roleIndex);
                }
            }
        } catch (SQLiteException e) {
            Log.e("Database", "Error getting role: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return role;
    }
    public long addStaff(Staff staff) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", staff.getUsername());
        values.put("password", staff.getPassword());
        values.put("role", staff.getRole());
        long result = db.insert("staff", null, values);
        db.close();
        return result;
    }
    // Method to update an existing staff member
    public int updateStaff(Staff staff) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", staff.getUsername());
        values.put("password", staff.getPassword());
        values.put("role", staff.getRole()); // Include role if it needs to be updated
        Log.i("USER ID", "STAFF ID SELECTED: " + staff.getId());
        int result = db.update("staff", values, "id=?", new String[]{String.valueOf(staff.getId())});
        db.close();

        if (result <= 0) {
            Log.e("DatabaseHelper", "Failed to update staff with id " + staff.getId());
        }
        return result;
    }
    // Method to delete a staff member
    public int deleteStaff(int staffId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("staff", "id=?", new String[]{String.valueOf(staffId)});
        db.close();
        return result;
    }
    // Method to add a new activity record
    public long addActivity(ActivityRecord activityRecord) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("client_name", activityRecord.getClientName());
        values.put("purpose", activityRecord.getPurpose());
        values.put("start_time", activityRecord.getStartTime());
        values.put("end_time", activityRecord.getEndTime());

        long result = db.insert("activities", null, values);
        db.close();
        return result;
    }
    public List<String> getAllClientsWithActivity() {
        List<String> clientActivities = new ArrayList<>();
        String query = "SELECT id, client_name, purpose, start_time, end_time FROM activities";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String clientActivity = "ID: " + cursor.getString(0) + "\n" +
                    "Client: " + cursor.getString(1) + "\n" +
                    "Purpose: " + cursor.getString(2) + "\n" +
                    "Start Time: " + cursor.getString(3) + "\n" +
                    "End Time: " + cursor.getString(4);
            clientActivities.add(clientActivity);
        }

        cursor.close();
        db.close();
        return clientActivities;
    }
    public List<String> getAllStaffDetails() {
        List<String> staffDetails = new ArrayList<>();
        String query = "SELECT id, username, password, role FROM staff";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String staffDetail = "ID: " + cursor.getString(0) + "\n" +
                    "Username: " + cursor.getString(1) + "\n" +
                    "Password: " + cursor.getString(2) + "\n" +
                    "Role: " + cursor.getString(3);
            staffDetails.add(staffDetail);
        }

        cursor.close();
        db.close();
        return staffDetails;
    }
}
