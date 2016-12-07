package ru.andreev_av.restclientexample.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Collection;

import ru.andreev_av.restclientexample.data.DbData.CityEntry;
import ru.andreev_av.restclientexample.data.DbData.CountryEntry;
import ru.andreev_av.restclientexample.objects.City;
import ru.andreev_av.restclientexample.objects.Country;

//TODO реализация с DbConnector
public class DbConnector {
    private static final String DB_NAME = "vkdata";
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

    private final Context mContext;


    private DBHelper mDBHelper;

    public DbConnector(Context context) {
        mContext = context;
        mDBHelper = new DBHelper(mContext, DB_NAME, null, DB_VERSION);
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper != null) mDBHelper.close();

    }

    // получить все данные из таблицы DB_TABLE_COUNTRY
    public Cursor getCountries() {
        SQLiteDatabase mDB = mDBHelper.getReadableDatabase();
        return mDB.query(CountryEntry.DB_TABLE_COUNTRY, null, null, null, null, null, null);
    }

    // получить все данные из таблицы DB_TABLE_CITY
    public Cursor getCities() {
        SQLiteDatabase mDB = mDBHelper.getReadableDatabase();
        return mDB.query(CityEntry.DB_TABLE_CITY, null, null, null, null, null, null);
    }

    // получить данные из таблицы DB_TABLE_CITY с переданным COLUMN_VK_COUNTRY_ID
    public Cursor getCities(int countryId) {
        SQLiteDatabase mDB = mDBHelper.getReadableDatabase();
        return mDB.query(CityEntry.DB_TABLE_CITY, null, CityEntry.COLUMN_VK_COUNTRY_CODE + " = ?", new String[]{String.valueOf(countryId)}, null, null, null);
    }

    // добавить/обновить страны в DB_TABLE_COUNTRY
    public void loadCountries(Collection<Country> countryList) {
        SQLiteDatabase mDB = mDBHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for (Country country : countryList) {
            cv.put(CountryEntry.COLUMN_COUNTRY_NAME, country.getCountryName());
            cv.put(CountryEntry.COLUMN_VK_COUNTRY_ID, country.getVkCountryId());
            // обновляем по cityId
            int updCount = mDB.update(CountryEntry.DB_TABLE_COUNTRY, cv, CountryEntry.COLUMN_VK_COUNTRY_ID + " = ?",
                    new String[]{String.valueOf(country.getVkCountryId())});
            if (updCount == 0) {
                mDB.insert(CountryEntry.DB_TABLE_COUNTRY, null, cv);
            }
        }
    }

    // добавить/обновить города в DB_TABLE
    public void loadCities(Collection<City> cityList, int countryId) {
        SQLiteDatabase mDB = mDBHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for (City city : cityList) {
            cv.put(CityEntry.COLUMN_CITY_NAME, city.getCityName());
            cv.put(CityEntry.COLUMN_VK_CITY_ID, city.getVkCityId());
            cv.put(CityEntry.COLUMN_VK_COUNTRY_CODE, countryId);
            // обновляем по cityId
            int updCount = mDB.update(CityEntry.DB_TABLE_CITY, cv, CityEntry.COLUMN_VK_CITY_ID + " = ?",
                    new String[]{String.valueOf(city.getVkCityId())});
            if (updCount == 0) {
                mDB.insert(CityEntry.DB_TABLE_CITY, null, cv);
            }
        }
    }

    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        private DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                         int version) {
            super(context, name, factory, version);
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_TABLE_CITY);
            db.execSQL(DB_CREATE_TABLE_COUNTRY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
