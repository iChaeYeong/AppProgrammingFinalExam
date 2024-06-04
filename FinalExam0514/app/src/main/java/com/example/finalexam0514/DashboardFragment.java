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

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private DatabaseHelper databaseHelper;
    private FloatingActionButton addPostButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        addPostButton = view.findViewById(R.id.addPostButton);
        databaseHelper = new DatabaseHelper(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadPosts();

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPostDialog();
            }
        });

        return view;
    }

    private void loadPosts() {
        Cursor cursor = databaseHelper.getAllPosts();
        List<Post> postList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                postList.add(new Post(id, title, content, author));
            } while (cursor.moveToNext());
            cursor.close();
        }

        postAdapter = new PostAdapter(postList, new PostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Post post) {
                showPostDetailsDialog(post);
            }

            @Override
            public void onItemDelete(Post post) {
                showDeletePostConfirmationDialog(post);
            }
        });
        recyclerView.setAdapter(postAdapter);
    }

    private void showAddPostDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("게시글 작성");

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_post, (ViewGroup) getView(), false);
        final EditText inputTitle = viewInflated.findViewById(R.id.inputTitle);
        final EditText inputContent = viewInflated.findViewById(R.id.inputContent);

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
                        String title = inputTitle.getText().toString();
                        String content = inputContent.getText().toString();

                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_prefs", getContext().MODE_PRIVATE);
                        String author = LoginActivity.getLoggedInUserId(sharedPreferences);

                        if (title.isEmpty() || content.isEmpty() || author == null) {
                            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                        } else {
                            boolean isInserted = databaseHelper.insertPost(title, content, author);
                            if (isInserted) {
                                loadPosts();
                                Toast.makeText(getContext(), "Post added successfully!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(getContext(), "Failed to add post.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

        dialog.show();
    }

    private void showPostDetailsDialog(Post post) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(post.getTitle());

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_post_details, (ViewGroup) getView(), false);
        final TextView postTitle = viewInflated.findViewById(R.id.postTitle);
        final TextView postContent = viewInflated.findViewById(R.id.postContent);
        final TextView postAuthor = viewInflated.findViewById(R.id.postAuthor);

        postTitle.setText(post.getTitle());
        postContent.setText(post.getContent());
        postAuthor.setText(post.getAuthor());

        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, null);

        builder.show();
    }

    private void showDeletePostConfirmationDialog(Post post) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("게시글 삭제");
        builder.setMessage("정말 이 게시글을 삭제하시겠습니까?");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.deletePost(post.getId());
                loadPosts();
                Toast.makeText(getContext(), "게시물 삭제완료", Toast.LENGTH_SHORT).show();
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