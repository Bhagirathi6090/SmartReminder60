package com.bhagi.smartreminder.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ReminderProvider extends ContentProvider {

    //Tag for the log messages
    public static final String LOG_TAG = ReminderProvider.class.getSimpleName();

    //URI matcher code for the content URI for the reminders table
    private static final int REMINDERS = 100;

    //URI matcher code for the content URI for a single reminder in the reminder table
    private static final int REMINDERS_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer
    static {
        // The calls to addURI()
        sUriMatcher.addURI(ReminderContract.CONTENT_AUTHORITY, ReminderContract.PATH_BOOK, REMINDERS);
        sUriMatcher.addURI(ReminderContract.CONTENT_AUTHORITY, ReminderContract.PATH_BOOK + "/#", REMINDERS_ID);
    }

    //Database helper object
    ReminderDbHelper reminderDbHelper;

    @Override
    public boolean onCreate() {
        reminderDbHelper = new ReminderDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get readable database
        SQLiteDatabase database = reminderDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDERS:
                //For the BOOKS code ,extracting the ID from the URI
                cursor = database.query(ReminderContract.ReminderEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case REMINDERS_ID:
                // For the BOOK_ID code, extracting out the ID from the URI.
                selection = ReminderContract.ReminderEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // Cursor containing that row of the table.
                cursor = database.query(ReminderContract.ReminderEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        //notification URI on the Cursor,
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDERS:
                return ReminderContract.ReminderEntry.CONTENT_LIST_TYPE;
            case REMINDERS_ID:
                return ReminderContract.ReminderEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDERS:
                return insertData(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertData(Uri uri,ContentValues values){
    // Check that the note is not null
            String date = values.getAsString(ReminderContract.ReminderEntry.COLUMN_REMINDER_DATE);
            if (date == null) {
                throw new IllegalArgumentException("Notes can not be empty");
            }

        String time = values.getAsString(ReminderContract.ReminderEntry.COLUMN_REMINDER_TIME);
        if (time == null) {
            throw new IllegalArgumentException("Notes can not be empty");
        }

        // Check that the note is not null
        String title = values.getAsString(ReminderContract.ReminderEntry.COLUMN_REMINDER_TITLE);
        if (title == null) {
            throw new IllegalArgumentException("Notes can not be empty");
        }

        // Check that the note is not null
        String note = values.getAsString(ReminderContract.ReminderEntry.COLUMN_REMINDER_NOTES);
        if (note == null) {
            throw new IllegalArgumentException("Notes can not be empty");
        }

        // Get writable database
        SQLiteDatabase database = reminderDbHelper.getWritableDatabase();

        // Insert the new book with the given values
        long id = database.insert(ReminderContract.ReminderEntry.TABLE_NAME, null, values);

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get writable database
        SQLiteDatabase database = reminderDbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case REMINDERS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(ReminderContract.ReminderEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REMINDERS_ID:
                // Delete a single row given by the ID in the URI
                selection = ReminderContract.ReminderEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(ReminderContract.ReminderEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
                 // If 1 or more rows were deleted, then notify all listeners that the data at the
                // given URI has changed
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                // Returned the number of rows deleted
                return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDERS:
                return updateBook(uri, contentValues, selection, selectionArgs);
            case REMINDERS_ID:
                selection = ReminderContract.ReminderEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(ReminderContract.ReminderEntry.COLUMN_REMINDER_DATE)) {
            String name = values.getAsString(ReminderContract.ReminderEntry.COLUMN_REMINDER_DATE);
            if (name == null) {
                throw new IllegalArgumentException("Book requires a name");
            }
        }

        if (values.containsKey(ReminderContract.ReminderEntry.COLUMN_REMINDER_TIME)) {
            String time = values.getAsString(ReminderContract.ReminderEntry.COLUMN_REMINDER_TIME);
            if (time == null) {
                throw new IllegalArgumentException("Book requires a name");
            }
        }

        // check that the price value is valid.
        if (values.containsKey(ReminderContract.ReminderEntry.COLUMN_REMINDER_TITLE)) {
            String title = values.getAsString(ReminderContract.ReminderEntry.COLUMN_REMINDER_TITLE);
            if (title == null) {
                throw new IllegalArgumentException("Book requires valid price");
            }
        }

        // check that the quantity value is valid.
        if (values.containsKey(ReminderContract.ReminderEntry.COLUMN_REMINDER_NOTES)) {
            // Check that the quantity is greater than or equal to 0 kg
            String quantity = values.getAsString(ReminderContract.ReminderEntry.COLUMN_REMINDER_NOTES);
            if (quantity == null) {
                throw new IllegalArgumentException("Book requires valid quantity");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writable database to update the data
        SQLiteDatabase database = reminderDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(ReminderContract.ReminderEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Returns the number of database rows affected by the update statement
        return rowsUpdated;
    }
}
