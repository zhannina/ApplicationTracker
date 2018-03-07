package com.example.zsarsenbayev.applicationtracker;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aware.Aware;
import com.aware.providers.Applications_Provider;
import com.aware.utils.DatabaseHelper;

import java.io.File;
import java.util.HashMap;

/**
 * Created by zsarsenbayev on 2/23/18.
 */

public class Provider extends ContentProvider{

    public static String AUTHORITY = Applications_Provider.AUTHORITY;

    public static final int DATABASE_VERSION = Applications_Provider.DATABASE_VERSION;

    //ContentProvider query indexes
    private static final int APPTRACKER = 1;
    private static final int APPTRACKER_ID = 2;

    public static final String DATABASE_NAME = Applications_Provider.DATABASE_NAME;

    public static final class ApplicationTracker_Data implements BaseColumns {
        private ApplicationTracker_Data() {}

        public static final Uri CONTENT_URI = Applications_Provider.Applications_Foreground.CONTENT_URI;
        public static final String CONTENT_TYPE = Applications_Provider.Applications_Foreground.CONTENT_TYPE;
        public static final String CONTENT_ITEM_TYPE = Applications_Provider.Applications_Foreground.CONTENT_ITEM_TYPE;

        public static final String _ID = "_id";
        public static final String TIMESTAMP = "timestamp";
        public static final String DEVICE_ID = "device_id";

    }

        public static final String[] DATABASE_TABLES = {"applications_foreground"};

        public static final String[] TABLES_FIELDS = {
                ApplicationTracker_Data._ID + " integer primary key autoincrement,"
                        + ApplicationTracker_Data.TIMESTAMP + " real default 0,"
                        + ApplicationTracker_Data.DEVICE_ID + " text default '',"
                        + "UNIQUE (" + ApplicationTracker_Data.TIMESTAMP
                        + "," + ApplicationTracker_Data.DEVICE_ID + ")"
        };

        private static UriMatcher sUriMatcher = null;
        private static HashMap<String, String> tableMap = null;
        private static DatabaseHelper databaseHelper = null;
        private static SQLiteDatabase database = null;

        /**
         * Initialise the ContentProvider
         */
        private boolean initializeDB() {
            if (databaseHelper == null) {
                databaseHelper = new DatabaseHelper( getContext(), DATABASE_NAME, null, DATABASE_VERSION, DATABASE_TABLES, TABLES_FIELDS );
            }
            if( databaseHelper != null && ( database == null || ! database.isOpen()) ) {
                database = databaseHelper.getWritableDatabase();
            }
            return( database != null && databaseHelper != null);
        }

        /**
         * Allow resetting the ContentProvider when updating/reinstalling AWARE
         */
        public static void resetDB(Context c ) {
            Log.d("AWARE", "Resetting " + DATABASE_NAME + "...");

            File db = new File(DATABASE_NAME);
            db.delete();
            databaseHelper = new DatabaseHelper(c, DATABASE_NAME, null, DATABASE_VERSION, DATABASE_TABLES, TABLES_FIELDS);
            if (databaseHelper != null) {
                database = databaseHelper.getWritableDatabase();
            }
        }


    @Override
    public boolean onCreate() {
        AUTHORITY = getContext().getPackageName() + ".provider.moodtracker"; //make AUTHORITY dynamic
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, DATABASE_TABLES[0], APPTRACKER); //URI for all records
        sUriMatcher.addURI(AUTHORITY, DATABASE_TABLES[0]+"/#", APPTRACKER_ID); //URI for a single record

        tableMap = new HashMap<String, String>();
        tableMap.put(ApplicationTracker_Data._ID, ApplicationTracker_Data._ID);
        tableMap.put(ApplicationTracker_Data.TIMESTAMP, ApplicationTracker_Data.TIMESTAMP);
        tableMap.put(ApplicationTracker_Data.DEVICE_ID, ApplicationTracker_Data.DEVICE_ID);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if( ! initializeDB() ) {
            Log.w(AUTHORITY,"Database unavailable...");
            return null;
        }
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case APPTRACKER:
                qb.setTables(DATABASE_TABLES[0]);
                qb.setProjectionMap(tableMap);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        try {
            Cursor c = qb.query(database, projection, selection, selectionArgs, null, null, sortOrder);
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        } catch (IllegalStateException e) {
            if (Aware.DEBUG) Log.e(Aware.TAG, e.getMessage());
            return null;
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case APPTRACKER:
                return ApplicationTracker_Data.CONTENT_TYPE;
            case APPTRACKER_ID:
                return ApplicationTracker_Data.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues new_values) {
        if( ! initializeDB() ) {
            Log.w(AUTHORITY,"Database unavailable...");
            return null;
        }

        ContentValues values = (new_values != null) ? new ContentValues(new_values) : new ContentValues();

        switch (sUriMatcher.match(uri)) {
            case APPTRACKER:
                long _id = database.insert(DATABASE_TABLES[0],ApplicationTracker_Data.DEVICE_ID, values);
                if (_id > 0) {
                    Uri dataUri = ContentUris.withAppendedId(ApplicationTracker_Data.CONTENT_URI, _id);
                    getContext().getContentResolver().notifyChange(dataUri, null);
                    return dataUri;
                }
                throw new SQLException("Failed to insert row into " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if( ! initializeDB() ) {
            Log.w(AUTHORITY,"Database unavailable...");
            return 0;
        }

        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case APPTRACKER:
                count = database.delete(DATABASE_TABLES[0], selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        if( ! initializeDB() ) {
            Log.w(AUTHORITY,"Database unavailable...");
            return 0;
        }

        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case APPTRACKER:
                count = database.update(DATABASE_TABLES[0], values, selection, selectionArgs);
                break;
            default:
                database.close();
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
