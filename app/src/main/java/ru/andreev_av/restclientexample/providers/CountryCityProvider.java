package ru.andreev_av.restclientexample.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;

import ru.andreev_av.restclientexample.data.DbData;
import ru.andreev_av.restclientexample.data.DbData.CityEntry;
import ru.andreev_av.restclientexample.data.DbData.CountryEntry;

public class CountryCityProvider extends ContentProvider {
    //// UriMatcher
    // общий Uri
    static final int URI_COUNTRIES = 1;
    static final int URI_CITIES = 2;
    private static final String DB_NAME = "vkData";
    private static final int DB_VERSION = 1;
    private static final String DB_CREATE_TABLE_COUNTRY =
            "create table " + CountryEntry.DB_TABLE_COUNTRY + "(" +
                    CountryEntry._ID + " integer primary key autoincrement, " +
                    CountryEntry.COLUMN_COUNTRY_NAME + " text, " +
                    CountryEntry.COLUMN_VK_COUNTRY_ID + " integer" +
                    ");";
    private static final String DB_CREATE_TABLE_CITY =
            "create table " + CityEntry.DB_TABLE_CITY + "(" +
                    CityEntry._ID + " integer primary key autoincrement, " +
                    CityEntry.COLUMN_CITY_NAME + " text, " +
                    CityEntry.COLUMN_VK_CITY_ID + " integer, " +
                    CityEntry.COLUMN_VK_COUNTRY_CODE + " integer" +
                    ");";
    // Uri с указанным ID
//    static final int URI_COUNTRIES_CODE_ID = 3;
    // описание и создание UriMatcher
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DbData.CONTENT_AUTHORITY, CountryEntry.COUNTRY_PATH, URI_COUNTRIES);
        uriMatcher.addURI(DbData.CONTENT_AUTHORITY, CityEntry.CITY_PATH, URI_CITIES);
//        uriMatcher.addURI(DbData.CONTENT_AUTHORITY, CityEntry.CITY_PATH + "/#", URI_COUNTRIES_CODE_ID);
    }

    DBHelper dbHelper;
    SQLiteDatabase db;
    private Context mContext;

    @Override
    public boolean onCreate() {
        mContext = getContext();
        dbHelper = new DBHelper(mContext);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // проверяем Uri
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case URI_COUNTRIES: // общий Uri
                db = dbHelper.getWritableDatabase();
                cursor = db.query(CountryEntry.DB_TABLE_COUNTRY, projection, selection,
                        selectionArgs, null, null, sortOrder);
                // просим ContentResolver уведомлять этот курсор
                // об изменениях данных в COUNTRY_CONTENT_URI
                cursor.setNotificationUri(mContext.getContentResolver(),
                        CountryEntry.COUNTRY_CONTENT_URI);
                break;
            case URI_CITIES: // общий Uri
                db = dbHelper.getWritableDatabase();
                cursor = db.query(CityEntry.DB_TABLE_CITY, projection, selection,
                        selectionArgs, null, null, sortOrder);
                // просим ContentResolver уведомлять этот курсор
                // об изменениях данных в CITY_CONTENT_URI
                cursor.setNotificationUri(mContext.getContentResolver(),
                        CityEntry.CITY_CONTENT_URI);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_COUNTRIES:
                return CountryEntry.COUNTRY_CONTENT_TYPE;
            case URI_CITIES:
                return CityEntry.CITY_CONTENT_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long rowID;
        Uri resultUri;
        switch (uriMatcher.match(uri)) {
            case URI_COUNTRIES:
                db = dbHelper.getWritableDatabase();
                rowID = db.insert(CountryEntry.DB_TABLE_COUNTRY, null, contentValues);
                resultUri = ContentUris.withAppendedId(CountryEntry.COUNTRY_CONTENT_URI, rowID);
                break;
            case URI_CITIES:
                db = dbHelper.getWritableDatabase();
                rowID = db.insert(CityEntry.DB_TABLE_CITY, null, contentValues);
                resultUri = ContentUris.withAppendedId(CityEntry.CITY_CONTENT_URI, rowID);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        // уведомляем ContentResolver, что данные по адресу resultUri изменились
        mContext.getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int cnt;
        switch (uriMatcher.match(uri)) {
            case URI_COUNTRIES:
                db = dbHelper.getWritableDatabase();
                cnt = db.update(CountryEntry.DB_TABLE_COUNTRY, values, selection, selectionArgs);
                break;
            case URI_CITIES:
                db = dbHelper.getWritableDatabase();
                cnt = db.update(CityEntry.DB_TABLE_CITY, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        mContext.getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_TABLE_CITY);
            db.execSQL(DB_CREATE_TABLE_COUNTRY);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
