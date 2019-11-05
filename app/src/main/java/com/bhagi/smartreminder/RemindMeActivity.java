package com.bhagi.smartreminder;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.bhagi.smartreminder.data.ReminderContract;
import com.bhagi.smartreminder.data.ReminderContract.ReminderEntry;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class RemindMeActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private ActionBar toolbar4;
    private EditText dateTextRemind;
    private EditText timeTextRemind;
    private EditText remindMeEditText;
    private TextView dateTextView;
    private TextView timeTextView;

    private Uri currentReminderUri;

    private boolean noteHasChanged = false;

    //Identifier for the notes data loader
    private static final int EXISTING_REMINDER_LOADER = 0;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            noteHasChanged = true;
            return false;
        }
    };

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_me);
        toolbar4 = getSupportActionBar();

        //EditTextViews of both time and date
        dateTextRemind = findViewById(R.id.date_picker_remind);
        timeTextRemind = findViewById(R.id.time_set_remind);

        //TextView of date and time
        dateTextView = findViewById(R.id.date_set_remind);
        timeTextView = findViewById(R.id.time_picker_remind);


        dateTextRemind.setEnabled(false);
        timeTextRemind.setEnabled(false);

        remindMeEditText = findViewById(R.id.editTextView_remind);

        Toolbar toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //intent for getting data from MainActivity
        final Intent intent = getIntent();
        currentReminderUri = intent.getData();

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDateButton();
            }
        });

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTimeButton();
            }
        });

        // creating a new note.
        if (currentReminderUri == null) {
            // This is a new note, so change the app bar to say "Add a Book"
            setTitle(getString(R.string.add));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            invalidateOptionsMenu();
        } else {
            setTitle(R.string.edit);

            // Initialize a loader to read the note data from the database
            // getLoaderManager().initLoader(EXISTING_REMINDER_LOADER, null,this);
            getLoaderManager().initLoader((EXISTING_REMINDER_LOADER), null, this);
        }

        remindMeEditText.setOnTouchListener(mTouchListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        getMenuInflater().inflate(R.menu.menu_remind, menu);
        return true;
    }

    private void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();

        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);

        boolean is24Hour = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);

                CharSequence timeCharSequence = DateFormat.format("hh:mm a", calendar1);
                timeTextRemind.setText(timeCharSequence);
            }
        }, HOUR, MINUTE, is24Hour);

        timePickerDialog.show();
    }

    private void handleDateButton() {
        Calendar calendar = Calendar.getInstance();

        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                String dateString = year + " " + month + " " + date;
                dateTextRemind.setText(dateString);

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);

                CharSequence dateCharSequence = DateFormat.format("EEEE, dd MMM yyyy", calendar1);
                dateTextRemind.setText(dateCharSequence);

            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();
    }

    public void saveNote() {
        String dateCreated = dateTextRemind.getText().toString().trim();
        String hourCreated = timeTextRemind.getText().toString().trim();
        String inputNote = remindMeEditText.getText().toString().trim();
        String titleString = "remind";

        // Check if this is supposed to be a new book
        // and check if all the fields in the editor are blank
        if (currentReminderUri == null &&
                TextUtils.isEmpty(inputNote)) {
            Toast.makeText(this, getString(R.string.provide_valid_data), Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(ReminderEntry.COLUMN_REMINDER_DATE, dateCreated);
        contentValues.put(ReminderEntry.COLUMN_REMINDER_TIME,hourCreated);
        contentValues.put(ReminderEntry.COLUMN_REMINDER_TITLE, titleString);
        contentValues.put(ReminderEntry.COLUMN_REMINDER_NOTES, inputNote);

        // Determine if this is a new or existing note by checking if mCurrentPetUri is null or not
        if (currentReminderUri == null) {
            // This is a NEW pet, so insert a new pet into the provider,
            Uri newUri = getContentResolver().insert(ReminderEntry.CONTENT_URI, contentValues);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.delete_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.reminder_saved),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(currentReminderUri, contentValues, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.delete_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.reminder_saved),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateData() {
        String name = remindMeEditText.getText().toString();
        String dateCreated = dateTextRemind.getText().toString();
        String hourCreated = timeTextRemind.getText().toString();

        boolean isValidate = true;
        if (name.equals("") || TextUtils.isEmpty(name)) {
            remindMeEditText.setError(getResources().getString(R.string.can_not_empty));
            isValidate = false;
        }
        if (dateCreated.equals("") || TextUtils.isEmpty(dateCreated)) {
            dateTextRemind.setError(getResources().getString(R.string.can_not_empty));
            isValidate = false;
        }
        if (hourCreated.equals("") || TextUtils.isEmpty(hourCreated)) {
            timeTextRemind.setError(getResources().getString(R.string.can_not_empty));
            isValidate = false;
        }

        return isValidate;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save_remind:
                if (validateData()) {
                    saveNote();
                    finish();
                } else {
                    Toast.makeText(RemindMeActivity.this, getResources().getString(R.string.enter_data), Toast.LENGTH_SHORT).show();
                }
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete_remind:
                showDeleteConfirmationDialog();
                return true;

            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                if (!noteHasChanged) {
                    NavUtils.navigateUpFromSameTask(RemindMeActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(RemindMeActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_this_reminder);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deletePet();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletePet() {
        // Only perform the delete if this is an existing book.
        if (currentReminderUri != null) {
            int rowsDeleted = getContentResolver().delete(currentReminderUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.delete_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.delete_reminder_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if (!noteHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new book, hide the "Delete" menu item.
        if (currentReminderUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete_remind);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String selection = ReminderContract.ReminderEntry.COLUMN_REMINDER_TITLE + "=?";

        String[] selectionArgs = {"remind"};

        // Since the editor shows all book attributes, define a projection that contains
        String[] projection = {
                ReminderEntry._ID,
                ReminderEntry.COLUMN_REMINDER_DATE,
                ReminderEntry.COLUMN_REMINDER_TIME,
                ReminderEntry.COLUMN_REMINDER_TITLE,
                ReminderEntry.COLUMN_REMINDER_NOTES};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                currentReminderUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                selection,                   // No selection clause
                selectionArgs,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int dateColumnIndex = cursor.getColumnIndex(ReminderEntry.COLUMN_REMINDER_DATE);
            int timeColumnIndex = cursor.getColumnIndex(ReminderEntry.COLUMN_REMINDER_TIME);
            int titleColumnIndex = cursor.getColumnIndex(ReminderEntry.COLUMN_REMINDER_TITLE);
            int notesColumnIndex = cursor.getColumnIndex(ReminderEntry.COLUMN_REMINDER_NOTES);

            String date = cursor.getString(dateColumnIndex);
            String time = cursor.getString(timeColumnIndex);
            String title = cursor.getString(titleColumnIndex);
            String notes = cursor.getString(notesColumnIndex);

            remindMeEditText.setText(notes);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        remindMeEditText.setText("");
    }

    public static Context getContextOfApplication() {
        return context;
    }

}
