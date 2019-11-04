package com.bhagi.smartreminder.ui.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.bhagi.smartreminder.R;
import com.bhagi.smartreminder.adapter.CustomArrayAdapter;
import com.bhagi.smartreminder.data.ReminderContract;

public class CalendarFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private EditText addSomething;
    private CalendarView calendar;
    private String Date;
    private CustomArrayAdapter customArrayAdapter;

    private View popupInputDialogView = null;
    private Button cancelUserDataButton = null;

    private static final int BOOK_LOADER = 0;

    private Uri currentReminderUri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        ListView noteListView = root.findViewById(R.id.calendar_list_view);
        customArrayAdapter = new CustomArrayAdapter(getActivity(), null);
        noteListView.setAdapter(customArrayAdapter);

        //EditText add something
        addSomething = root.findViewById(R.id.title_calender_txt);
        calendar = root.findViewById(R.id.calenderView);
        // Add Listener in calendar
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(
                    @NonNull CalendarView view,
                    int year,
                    int month,
                    int dayOfMonth) {

                // Store the value of date with
                // format in String type Variable
                // Add 1 in month because month
                // index is start with 0
                Date = dayOfMonth + "/" + (month + 1) + "/" + year;

            }
        });

        Button addButton = root.findViewById(R.id.add_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               saveNote();
            }
        });
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//                popupInputDialogView = layoutInflater.inflate(R.layout.pop_up_dialog, null);
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//                alertDialogBuilder.setTitle("Do you want to update something?");
//                alertDialogBuilder.setCancelable(false);
//
//                // Set the inflated layout view object to the AlertDialog builder.
//                alertDialogBuilder.setView(popupInputDialogView);
//
//                // Create AlertDialog and show.
//                final AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//
//                cancelUserDataButton = popupInputDialogView.findViewById(R.id.button_cancel_user_data);
//
//                cancelUserDataButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        alertDialog.cancel();
//                    }
//                });
//            }
//        });
        getLoaderManager().initLoader(BOOK_LOADER, null, this);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_deleteAll:
                deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveNote() {
        String dateCreated = Date;
        String inputNote = addSomething.getText().toString().trim();
        String titleString = "calendar";
        String createdAt = dateCreated;

        ContentValues contentValues = new ContentValues();
        contentValues.put(ReminderContract.ReminderEntry.COLUMN_REMINDER_DATE, createdAt);
        contentValues.put(ReminderContract.ReminderEntry.COLUMN_REMINDER_TITLE, titleString);
        contentValues.put(ReminderContract.ReminderEntry.COLUMN_REMINDER_NOTES, inputNote);

        Uri newUri = getActivity().getContentResolver().insert(ReminderContract.ReminderEntry.CONTENT_URI, contentValues);

        if (newUri == null) {
            Toast.makeText(getActivity(), getString(R.string.editor_insert_note_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), getString(R.string.editor_insert_note_successful),
                    Toast.LENGTH_SHORT).show();
            addSomething.setText("");
        }
    }

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader ( int id, @Nullable Bundle args){
            String selection = ReminderContract.ReminderEntry.COLUMN_REMINDER_TITLE + "=?";
            String[] selectionArgs = {"calendar"};
            String[] projection = {
                    ReminderContract.ReminderEntry._ID,
                    ReminderContract.ReminderEntry.COLUMN_REMINDER_DATE,
                    ReminderContract.ReminderEntry.COLUMN_REMINDER_TITLE,
                    ReminderContract.ReminderEntry.COLUMN_REMINDER_NOTES};

            return new CursorLoader(getActivity(),
                    ReminderContract.ReminderEntry.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    null);
        }

        @Override
        public void onLoadFinished (@NonNull Loader < Cursor > loader, Cursor data){
            customArrayAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset (@NonNull Loader < Cursor > loader) {
            customArrayAdapter.swapCursor(null);
        }

        private void deleteAllPets () {
            String selection = ReminderContract.ReminderEntry.COLUMN_REMINDER_TITLE + "=?";
            String[] selectionArgs = {"calendar"};

            Context context = getActivity().getApplicationContext();
            int rowsDeleted = context.getContentResolver().delete(ReminderContract.ReminderEntry.CONTENT_URI, selection, selectionArgs);
        }
    }