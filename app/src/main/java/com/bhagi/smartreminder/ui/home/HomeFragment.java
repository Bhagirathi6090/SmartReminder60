package com.bhagi.smartreminder.ui.home;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

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
import android.widget.TextView;

import com.bhagi.smartreminder.EditorActivity;
import com.bhagi.smartreminder.MainActivity;
import com.bhagi.smartreminder.R;
import com.bhagi.smartreminder.adapter.HomeCursorAdapter;
import com.bhagi.smartreminder.data.ReminderContract;
import com.bhagi.smartreminder.ui.calendar.CalendarViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    //CursorAdapter object
    private HomeCursorAdapter homeCursorProvider;

    private static final int BOOK_LOADER = 0;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the ListView which will be populated with the book data
        ListView noteListView = root.findViewById(R.id.list_view);


        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = root.findViewById(R.id.empty_view);
        noteListView.setEmptyView(emptyView);

        homeCursorProvider = new HomeCursorAdapter(getActivity(), null);
        noteListView.setAdapter(homeCursorProvider);

        // Setup the item click listener
        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EditorActivity.class);

                Uri currentPetUri = ContentUris.withAppendedId(ReminderContract.ReminderEntry.CONTENT_URI, id);
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });

        noteListView.setItemsCanFocus(true);
        getLoaderManager().initLoader(BOOK_LOADER, null, this);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDetail();
            }
        });

        return root;
    }


    public void updateDetail() {
        Intent intent = new Intent(getActivity(), EditorActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_deleteAll:
                deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String selection = ReminderContract.ReminderEntry.COLUMN_REMINDER_TITLE +"=?";
        String[] selectionArgs = {"notes"};
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
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        homeCursorProvider.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        homeCursorProvider.swapCursor(null);
    }

    private void deleteAllPets() {
        String selection = ReminderContract.ReminderEntry.COLUMN_REMINDER_TITLE +"=?";
        String[] selectionArgs = {"notes"};

        Context context = getActivity().getApplicationContext();
        int rowsDeleted = context.getContentResolver().delete(ReminderContract.ReminderEntry.CONTENT_URI, selection, selectionArgs);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from reminder database");
    }
}