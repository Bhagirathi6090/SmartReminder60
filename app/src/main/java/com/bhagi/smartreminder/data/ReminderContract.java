package com.bhagi.smartreminder.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ReminderContract {

    public static final String CONTENT_AUTHORITY = "com.bhagi.smartreminder";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_BOOK = "reminder";

   private ReminderContract(){}

    public static final class ReminderEntry implements BaseColumns {

        //The content URI to access the book data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOK);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOK;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOK;

        //Name of the table
        public final static String TABLE_NAME = "reminder";

        //primary key or Unique ID of table
        public final static String _ID = BaseColumns._ID;

        //Column date
        public final static String COLUMN_REMINDER_DATE = "date";

        //Column time
        public final static String COLUMN_REMINDER_TIME = "time";

        //Column title of the notes
        public final static String COLUMN_REMINDER_TITLE = "title";

        //Column notes
        public final static String COLUMN_REMINDER_NOTES = "notes";

    }

}
