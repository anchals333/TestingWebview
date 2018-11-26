package com.example.testingwebview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    private List<BookmarkList> bookmarkList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        TextView tvEmptyBookmark = findViewById(R.id.empty_notes_view);
        DatabaseHelper db = new DatabaseHelper(this);
        bookmarkList.addAll(db.getAllBoomark());
        int count = db.getBookmarkCount();
        if (count == 0) {
            tvEmptyBookmark.setVisibility(View.VISIBLE);
        } else {
            tvEmptyBookmark.setVisibility(View.GONE);
        }
        BookmarkAdapter mAdapter = new BookmarkAdapter(this, bookmarkList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView,
                new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("url", bookmarkList.get(position).getUrl());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
}
