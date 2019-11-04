package com.bhagi.smartreminder.adapter;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.bhagi.smartreminder.R;
import com.bhagi.smartreminder.data.ReminderContract;

public class CustomArrayAdapter extends CursorAdapter {

    public CustomArrayAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView dateTextView = view.findViewById(R.id.listView_date);
        TextView notesTextView = view.findViewById(R.id.listView_desc);

        int dateColumnIndex = cursor.getColumnIndex(ReminderContract.ReminderEntry.COLUMN_REMINDER_DATE);
        int titleColumnIndex = cursor.getColumnIndex(ReminderContract.ReminderEntry.COLUMN_REMINDER_TITLE);
        int notesColumnIndex = cursor.getColumnIndex(ReminderContract.ReminderEntry.COLUMN_REMINDER_NOTES);
        int index = cursor.getColumnIndex(ReminderContract.ReminderEntry._ID);

        String createdAt = cursor.getString(dateColumnIndex);
        String title = cursor.getString(titleColumnIndex);
        String notes = cursor.getString(notesColumnIndex);
        final long id = cursor.getLong(index);

        String date = createdAt;
        String time = createdAt;

        ContentValues values = new ContentValues();
        Uri uri = ContentUris.withAppendedId(ReminderContract.ReminderEntry.CONTENT_URI, id);
        context.getContentResolver().update(
                uri,
                values,
                ReminderContract.ReminderEntry._ID + "=?",
                new String[]{String.valueOf(ContentUris.parseId(uri))});

        dateTextView.setText(date);
        notesTextView.setText(notes);
    }
}

