package com.example.finalexam0514;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;

public class CalenderActivity extends AppCompatActivity {    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private CalendarView calendarView;
    private EditText editTextMemo;
    private Button buttonSave;
    private Button buttonDelete;
    private TextView textViewMemo;

    private HashMap<String, ArrayList<String>> memoMap = new HashMap<>();
    private String selectedDate;
    private boolean isDrawerAdded = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
            return insets;
        });
        calendarView = findViewById(R.id.calendarView);
        editTextMemo = findViewById(R.id.editTextMemo);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);
        textViewMemo = findViewById(R.id.textViewMemo);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = year + "/" + (month + 1) + "/" + dayOfMonth;
            displayMemos();
        });

        buttonSave.setOnClickListener(v -> {
            String memo = editTextMemo.getText().toString();
            if (selectedDate != null && !memo.isEmpty()) {
                ArrayList<String> memos = memoMap.getOrDefault(selectedDate, new ArrayList<>());
                memos.add(memo);
                memoMap.put(selectedDate, memos);
                displayMemos();
                editTextMemo.setText("");
                Toast.makeText(CalenderActivity.this, "메모가 저장되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CalenderActivity.this, "날짜를 선택하고 메모를 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        buttonDelete.setOnClickListener(v -> {
            if (selectedDate != null && memoMap.containsKey(selectedDate)) {
                ArrayList<String> memos = memoMap.get(selectedDate);
                if (!memos.isEmpty()) {
                    showDeleteDialog(memos);
                } else {
                    Toast.makeText(CalenderActivity.this, "삭제할 메모가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CalenderActivity.this, "삭제할 메모가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // DrawerLayout과 NavigationView 할당
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // 툴바 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 사이드바 리스너 설정
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent = null;
            if (itemId == R.id.nav_main) {
                intent = new Intent(CalenderActivity.this, MainActivity.class);
            } else if (itemId == R.id.nav_calender) {
                intent = new Intent(CalenderActivity.this, CalenderActivity.class);
            } else if (itemId == R.id.nav_logout) {
                intent = new Intent(CalenderActivity.this, LoginActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
                finish(); // 현재 액티비티를 종료하여 뒤로 가기 버튼 누를 때 이전 화면으로 돌아가지 않도록 합니다.
            }
            drawerLayout.closeDrawer(navigationView);
            return true;
        });

        // 툴바의 토글 버튼에 리스너 설정
        findViewById(R.id.toggleButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });
    }

    // 토글 드로어 상태
    private void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView); // 사이드바 열려 있다면 닫기
        } else {
            drawerLayout.openDrawer(navigationView); // 사이드바 닫혀 있다면 열기
        }
    }
    private void displayMemos() {
        ArrayList<String> memos = memoMap.get(selectedDate);
        if (memos != null && !memos.isEmpty()) {
            StringBuilder memoText = new StringBuilder();
            for (String memo : memos) {
                memoText.append(memo).append("\n");
            }
            textViewMemo.setText(memoText.toString().trim());
        } else {
            textViewMemo.setText("메모가 없습니다.");
        }
    }

    private void showDeleteDialog(ArrayList<String> memos) {
        String[] memoArray = memos.toArray(new String[0]);
        boolean[] checkedItems = new boolean[memoArray.length];
        new AlertDialog.Builder(this)
                .setTitle("삭제할 메모 선택")
                .setMultiChoiceItems(memoArray, checkedItems, (dialog, which, isChecked) -> checkedItems[which] = isChecked)
                .setPositiveButton("삭제", (dialog, which) -> {
                    for (int i = checkedItems.length - 1; i >= 0; i--) {
                        if (checkedItems[i]) {
                            memos.remove(i);
                        }
                    }
                    if (memos.isEmpty()) {
                        memoMap.remove(selectedDate);
                    } else {
                        memoMap.put(selectedDate, memos);
                    }
                    displayMemos();
                    Toast.makeText(CalenderActivity.this, "선택한 메모가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("취소", null)
                .show();
    }
}