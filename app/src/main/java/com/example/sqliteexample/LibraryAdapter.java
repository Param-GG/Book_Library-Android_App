package com.example.sqliteexample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.BookViewHolder> {

    private Context context;
    Animation translate_anim;
    Activity activity;

    public LibraryAdapter(Activity activity) {
        this.activity = activity;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private DiffUtil.ItemCallback<Book> diffCallback = new DiffUtil.ItemCallback<Book>() {
        @Override
        public boolean areItemsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
            return oldItem.getBookId().equals(newItem.getBookId());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
            return oldItem.equals(newItem);
        }
    };

    public AsyncListDiffer<Book> differ = new AsyncListDiffer<Book>(this, diffCallback);

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new BookViewHolder(LayoutInflater.from(context).inflate(R.layout.my_row, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        List<Book> bookArrayList = differ.getCurrentList();
        Book book = bookArrayList.get(position);
        TextView bookId = holder.itemView.findViewById(R.id.book_id_txt);
        TextView bookTitle = holder.itemView.findViewById(R.id.book_title_txt);
        TextView bookAuthor = holder.itemView.findViewById(R.id.book_author_txt);
        TextView bookPages = holder.itemView.findViewById(R.id.book_pages_txt);
        LinearLayout mainLayout = holder.itemView.findViewById(R.id.mainLayout);

        bookId.setText(String.valueOf(position+1));
        bookTitle.setText(book.getBookTitle());
        bookAuthor.setText(book.getBookAuthor());
        bookPages.setText(book.getBookPages());

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", book.getBookId());
                intent.putExtra("title", book.getBookTitle());
                intent.putExtra("author", book.getBookAuthor());
                intent.putExtra("pages", book.getBookPages());
                activity.startActivityForResult(intent, 1);
            }
        });
        translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
        mainLayout.setAnimation(translate_anim);
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

}
