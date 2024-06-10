package com.example.finalexam0514;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.fragment.app.FragmentActivity;

public class UserDataManager {

    private DatabaseHelper dbHelper;

    public UserDataManager(Context context) {
        dbHelper = new DatabaseHelper((FragmentActivity) context);
    }

    public void addUser(String username, String email, String petBreed, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);
        values.put("pet_breed", petBreed);
        values.put("password", password); // 해시된 비밀번호로 저장

        db.insert("users", null, values);
        db.close();
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        User user = null;
        Cursor cursor = db.query("users", new String[]{"username", "email", "pet_breed", "password"}, "email=?", new String[]{email}, null, null, null);

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("username"));
            @SuppressLint("Range") String userEmail = cursor.getString(cursor.getColumnIndex("email"));
            @SuppressLint("Range") String petBreed = cursor.getString(cursor.getColumnIndex("pet_breed"));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password")); // 비밀번호 가져오기
            user = new User(username, userEmail, petBreed, password); // 비밀번호를 사용자 객체에 포함
        }

        cursor.close();
        db.close();

        return user;
    }

    private String hashPassword(String password) {
        // 비밀번호 해시 로직을 여기에 구현 (예: SHA-256 사용)
        // 간단한 예를 위해, 여기서는 원래 비밀번호를 그대로 반환합니다.
        // 실제 사용 시에는 안전한 해시 알고리즘을 사용하세요.
        return password;
    }
}