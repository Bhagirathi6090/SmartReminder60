package com.bhagi.smartreminder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bhagi.smartreminder.data.ReminderContract.ReminderEntry;

public class ReminderDbHelper extends SQLiteOpenHelper {

    //Name of the database
    private static final String DATABASE_NAME = "reminder.db";

    //database version
    private static final int DATABASE_VERSION = 1;

    //Constructor
    public ReminderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //This string create the sql table
        String SQL_CREATE_REMINDER_TABLE = "CREATE TABLE " + ReminderEntry.TABLE_NAME + " ("
                + ReminderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ReminderEntry.COLUMN_REMINDER_DATE + " TEXT NOT NULL,"
                + ReminderEntry.COLUMN_REMINDER_TIME + " TEXT NOT NULL,"
                + ReminderEntry.COLUMN_REMINDER_TITLE + " TEXT NOT NULL,"
                + ReminderEntry.COLUMN_REMINDER_NOTES + " TEXT NOT NULL);";

        // String SQL_DROP_TABLE="DROP TABLE "+ BookEntry.TABLE_NAME;
        sqLiteDatabase.execSQL(SQL_CREATE_REMINDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
