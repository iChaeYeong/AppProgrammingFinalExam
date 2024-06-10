package com.example.finalexam0514;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NotificationsFragment extends Fragment {

    private TextView userIdTextView, userHandleTextView, userPetBreedTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        userIdTextView = view.findViewById(R.id.userIdTextView);
        userHandleTextView = view.findViewById(R.id.userHandleTextView);
        userPetBreedTextView = view.findViewById(R.id.userPetBreedTextView);

        Bundle args = getArguments();
        if (args != null) {
            String email = args.getString("USER_EMAIL");
            if (email != null) {
                DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                User user = dbHelper.getUserByEmail(email);
                if (user != null) {
                    userIdTextView.setText("ID: " + user.getUsername());
                    userHandleTextView.setText("@: " + user.getUsername());
                    userPetBreedTextView.setText("Pet Breed: " + user.getPetBreed());
                } else {
                    userIdTextView.setText("User not found");
                }
            } else {
                userIdTextView.setText("User email not provided");
            }
        }

        return view;
    }
}