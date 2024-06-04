package com.example.finalexam0514;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    private List<Shop> shopList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Shop shop);
        void onItemDelete(Shop shop);
    }

    public ShopAdapter(List<Shop> shopList, OnItemClickListener listener) {
        this.shopList = shopList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        Shop shop = shopList.get(position);
        holder.bind(shop, listener);
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView address;
        public TextView info;
        public TextView openTime;
        public ImageButton deleteButton;

        public ShopViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.shop_name_item);
            address = itemView.findViewById(R.id.shop_adress_item);
            info = itemView.findViewById(R.id.shop_inpo_item);
            openTime = itemView.findViewById(R.id.shop_opentime_item);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(final Shop shop, final OnItemClickListener listener) {
            name.setText(shop.getName());
            address.setText(shop.getAddress());
            info.setText(shop.getInfo());
            openTime.setText(shop.getOpenTime());
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemDelete(shop);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(shop);
                }
            });
        }
    }
}