package com.example.finalexam0514;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class fragment_home_clothing extends Fragment {

    private RecyclerView recyclerView;
    private ShopAdapter shopAdapter;
    private DatabaseHelper databaseHelper;
    private FloatingActionButton addShopButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_clothing, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        addShopButton = view.findViewById(R.id.addshopButton);
        databaseHelper = new DatabaseHelper(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadShops();

        addShopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddShopDialog();
            }
        });

        return view;
    }

    private void loadShops() {
        Cursor cursor = databaseHelper.getAllShops();
        List<Shop> shopList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String info = cursor.getString(cursor.getColumnIndexOrThrow("info"));
                String openTime = cursor.getString(cursor.getColumnIndexOrThrow("openTime"));
                String user = cursor.getString(cursor.getColumnIndexOrThrow("user"));
                shopList.add(new Shop(id, name, address, info, openTime, user));
            } while (cursor.moveToNext());
            cursor.close();
        }

        shopAdapter = new ShopAdapter(shopList, new ShopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Shop shop) {
                showShopDetailsDialog(shop);
            }

            @Override
            public void onItemDelete(Shop shop) {
                showDeleteShopConfirmationDialog(shop);
            }
        });
        recyclerView.setAdapter(shopAdapter);
    }

    private void showAddShopDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("가게 정보 추가");

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.shop_info_add, (ViewGroup) getView(), false);
        final EditText inputName = viewInflated.findViewById(R.id.shop_name);
        final EditText inputAddress = viewInflated.findViewById(R.id.shop_adress);
        final EditText inputInfo = viewInflated.findViewById(R.id.shop_inpo);
        final EditText inputOpenTime = viewInflated.findViewById(R.id.shop_opentime);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, null);
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = inputName.getText().toString();
                        String address = inputAddress.getText().toString();
                        String info = inputInfo.getText().toString();
                        String openTime = inputOpenTime.getText().toString();

                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_prefs", getContext().MODE_PRIVATE);
                        String user = LoginActivity.getLoggedInUserId(sharedPreferences);

                        if (name.isEmpty() || address.isEmpty() || info.isEmpty() || openTime.isEmpty() || user == null) {
                            Toast.makeText(getContext(), "모든 필드를 채워주세요", Toast.LENGTH_SHORT).show();
                        } else {
                            boolean isInserted = databaseHelper.insertShop(name, address, info, openTime, user);
                            if (isInserted) {
                                loadShops();
                                Toast.makeText(getContext(), "가게 정보가 추가되었습니다!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(getContext(), "가게 정보 추가에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

        dialog.show();
    }

    private void showShopDetailsDialog(Shop shop) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(shop.getName());

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.shop_info_details, (ViewGroup) getView(), false);
        final TextView shopName = viewInflated.findViewById(R.id.shop_name_text);
        final TextView shopAddress = viewInflated.findViewById(R.id.shop_adress_text);
        final TextView shopInfo = viewInflated.findViewById(R.id.shop_inpo_text);
        final TextView shopOpenTime = viewInflated.findViewById(R.id.shop_opentime_text);

        shopName.setText(shop.getName());
        shopAddress.setText(shop.getAddress());
        shopInfo.setText(shop.getInfo());
        shopOpenTime.setText(shop.getOpenTime());

        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, null);

        builder.show();
    }

    private void showDeleteShopConfirmationDialog(final Shop shop) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("가게 정보 삭제");
        builder.setMessage("정말 이 가게 정보를 삭제하시겠습니까?");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.deleteShop(shop.getId());
                loadShops();
                Toast.makeText(getContext(), "가게 정보가 삭제되었습니다", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}