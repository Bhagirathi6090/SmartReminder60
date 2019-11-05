package com.bhagi.smartreminder.ui.remindMe;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.bhagi.smartreminder.R;
import com.bhagi.smartreminder.RemindMeActivity;
import com.bhagi.smartreminder.adapter.HomeCursorAdapter;
import com.bhagi.smartreminder.data.ReminderContract;

public class RemindMeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    //CursorAdapter object
    private HomeCursorAdapter homeProviderCursor;

    private static final int BOOK_LOADER = 0;

    private ListView noteListView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.fragment_remind, container, false);

        // Find the ListView which will be populated with the book data
        noteListView = root.findViewById(R.id.remind_list_view);

//        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = root.findViewById(R.id.write_a_reminder);
        noteListView.setEmptyView(emptyView);

        homeProviderCursor = new HomeCursorAdapter(getActivity(), null);
        noteListView.setAdapter(homeProviderCursor);

        // Setup the item click listener
        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RemindMeActivity.class);

                Uri currentRemindUri = ContentUris.withAppendedId(ReminderContract.ReminderEntry.CONTENT_URI, id);
                intent.setData(currentRemindUri);
                startActivity(intent);
            }
        });

        noteListView.setItemsCanFocus(true);
        getLoaderManager().initLoader(BOOK_LOADER, null, this);

        Button addReminder = root.findViewById(R.id.write_a_reminder);
        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDetail();
            }
        });
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_birthday_remind, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void updateDetail() {
        Intent intent = new Intent(getActivity(), RemindMeActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_activity:
                deleteAllPets();
                return true;
            case R.id.action_save_activity:
                updateDetail();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String selection = ReminderContract.ReminderEntry.COLUMN_REMINDER_TITLE +"=?";
        String[] selectionArgs = {"remind"};

        String[] projection = {
                ReminderContract.ReminderEntry._ID,
                ReminderContract.ReminderEntry.COLUMN_REMINDER_DATE,
                ReminderContract.ReminderEntry.COLUMN_REMINDER_TIME,
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
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        homeProviderCursor.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        homeProviderCursor.swapCursor(null);
    }

    private void deleteAllPets() {
        String selection = ReminderContract.ReminderEntry.COLUMN_REMINDER_TITLE +"=?";
        String[] selectionArgs = {"remind"};

        Context context = getActivity().getApplicationContext();
        int rowsDeleted = context.getContentResolver().delete(ReminderContract.ReminderEntry.CONTENT_URI, selection, selectionArgs);

        // Show a toast message depending on whether or not the delete was successful.
        if (rowsDeleted == 0) {
            Toast.makeText(getActivity(), getString(R.string.delete_reminder_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), getString(R.string.all_records_deleted_failed),
                    Toast.LENGTH_SHORT).show();
        }
    }

}