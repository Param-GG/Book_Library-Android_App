package com.example.sqliteexample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton add_button;
    private MyDatabaseHelper myDB;
    private String book_id, book_title, book_author, book_pages;
    private ArrayList<Book> bookList = new ArrayList<Book>();
    private LibraryAdapter libraryAdapter;
    private ArrayList<Book> recyclerBookList = new ArrayList<Book>();

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        myDB = new MyDatabaseHelper(MainActivity.this);

        recyclerBookList = storeDataInArrays();

        libraryAdapter = new LibraryAdapter(MainActivity.this);
        recyclerView.setAdapter(libraryAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        if(recyclerBookList!=null){
            libraryAdapter.differ.submitList(recyclerBookList);
        }
        else{
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    private ArrayList<Book> storeDataInArrays() {
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount()!=0) {
            while (cursor.moveToNext()) {
                book_id = cursor.getString(0);
                book_title = cursor.getString(1);
                book_author = cursor.getString(2);
                book_pages = cursor.getString(3);
                bookList.add(new Book(book_id, book_title, book_author, book_pages));
            }
            return bookList;
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.delete_all){
            confirmDialogue();
        }
        if(item.getItemId()==R.id.explore){
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, "Top new books to read");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to empty the library?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDB.deleteAllData();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}