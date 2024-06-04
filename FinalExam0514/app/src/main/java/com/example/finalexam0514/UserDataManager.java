package com.example.finalexam0514;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDataManager {

    private DatabaseHelper dbHelper;

    public UserDataManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void addUser(String email, String password, String username, String petBreed) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("password", password);
        values.put("username", username);
        values.put("pet_breed", petBreed);

        db.insert("users", null, values);
        db.close();
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        User user = null;
        Cursor cursor = db.query("users", new String[]{"email", "username", "pet_breed"}, "email=?", new String[]{email}, null, null, null);

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String userEmail = cursor.getString(cursor.getColumnIndex("email"));
            @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("username"));
            @SuppressLint("Range") String petBreed = cursor.getString(cursor.getColumnIndex("pet_breed"));
            user = new User(userEmail, username, petBreed);
        }

        cursor.close();
        db.close();

        return user;
    }
}