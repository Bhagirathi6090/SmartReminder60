package com.bhagi.smartreminder.adapter;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.bhagi.smartreminder.R;
import com.bhagi.smartreminder.data.ReminderContract;

public class HomeCursorAdapter extends CursorAdapter {

    public HomeCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.display_home, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView dateTextView = view.findViewById(R.id.date_textView);
        TextView timeTextView = view.findViewById(R.id.time_textView);
        TextView titleTextView = view.findViewById(R.id.title_textView);
        TextView notesTextView = view.findViewById(R.id.notes_text);

        int dateColumnIndex = cursor.getColumnIndex(ReminderContract.ReminderEntry.COLUMN_REMINDER_DATE);
        int titleColumnIndex = cursor.getColumnIndex(ReminderContract.ReminderEntry.COLUMN_REMINDER_TITLE);
        int notesColumnIndex = cursor.getColumnIndex(ReminderContract.ReminderEntry.COLUMN_REMINDER_NOTES);
        int index = cursor.getColumnIndex(ReminderContract.ReminderEntry._ID);

        String createdAt = cursor.getString(dateColumnIndex);
        String title = cursor.getString(titleColumnIndex);
        String notes = cursor.getString(notesColumnIndex);
        final long id = cursor.getLong(index);

        String date = createdAt.substring(0,3)+","+createdAt.substring(8,15);
        String time = createdAt.substring(21,29);

        ContentValues values = new ContentValues();
        Uri uri = ContentUris.withAppendedId(ReminderContract.ReminderEntry.CONTENT_URI, id);
        context.getContentResolver().update(
                uri,
                values,
                ReminderContract.ReminderEntry._ID + "=?",
                new String[]{String.valueOf(ContentUris.parseId(uri))});

        dateTextView.setText(date);
        timeTextView.setText(time);
        titleTextView.setText(title);
        notesTextView.setText(notes);
    }
}
