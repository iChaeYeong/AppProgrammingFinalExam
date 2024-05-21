package com.example.finalexam0514;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private boolean isDrawerAdded = true; // 사이드바가 추가되었는지의 상태
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Window insets 적용
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
            return insets;
        });

        // DrawerLayout과 NavigationView, BottomNavigationView 할당
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();

        // 툴바 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 툴바의 기본 타이틀 비활성화

        // 사이드바 설정 초기화
        setupDrawer();

        // 툴바의 토글 버튼에 리스너 설정
        findViewById(R.id.toggleButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });

        // 사이드바 리스너 설정
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // 'Home' 클릭 시 처리
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.nav_dashboard) {
                // 'Dashboard' 클릭 시 처리
                replaceFragment(new DashboardFragment());
            } else if (itemId == R.id.nav_notifications) {
                // 'Notifications' 클릭 시 처리
                replaceFragment(new NotificationsFragment());
            }
            drawerLayout.closeDrawer(navigationView);
            return true;
        });

        // 하단 네비게이션 바 리스너 설정
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // 'Home' 클릭 시 처리
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.nav_dashboard) {
                // 'Dashboard' 클릭 시 처리
                replaceFragment(new DashboardFragment());
            } else if (itemId == R.id.nav_notifications) {
                // 'Notifications' 클릭 시 처리
                replaceFragment(new NotificationsFragment());
            }
            return true;
        });

        // 기본 프래그먼트 설정
        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
        }
    }

    // 사이드바 설정 및 제거
    private void setupDrawer() {
        if (isDrawerAdded) {
            if (navigationView.getParent() == null) {
                drawerLayout.addView(navigationView);
            }
        } else {
            if (navigationView.getParent() != null) {
                drawerLayout.removeView(navigationView);
            }
        }
    }

    // 토글 드로어 상태
    private void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView); // 사이드바 열려 있다면 닫기
        } else {
            drawerLayout.openDrawer(navigationView); // 사이드바 닫혀 있다면 열기
        }
    }

    // 프래그먼트 교체
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}