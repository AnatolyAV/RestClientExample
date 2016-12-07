package ru.andreev_av.restclientexample.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ru.andreev_av.restclientexample.R;
import ru.andreev_av.restclientexample.data.DbData.CityEntry;
import ru.andreev_av.restclientexample.data.DbData.CountryEntry;
import ru.andreev_av.restclientexample.helpers.ServiceHelper;
import ru.andreev_av.restclientexample.services.ProcessorService;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    public final static int STATUS_START = 100;
    public final static int STATUS_FINISH = 200;
    public final static int COUNTRY_LOADER = 1;
    public final static int CITY_LOADER = 2;
    public final static String PARAM_STATUS = "status";
    public final static String BROADCAST_ACTION = "ru.andreev_av.restclientexample.broadcast";
    private static int countryId = -1;
    private final IntentFilter mFilter = new IntentFilter(BROADCAST_ACTION);
    private Spinner spCountry;
    private ProgressDialog dialog;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int status = intent.getIntExtra(PARAM_STATUS, 0);

            // Ловим сообщения о старте задач
            if (status == STATUS_START) {
                dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage(getResources().getString(R.string.loading));
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }

            // Ловим сообщения об окончании задач
            if (status == STATUS_FINISH) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Bundle extras = intent.getExtras();
                boolean success = extras.getBoolean(ProcessorService.Extras.RESULT_EXTRA);
                //TODO реализация с DbConnector
//                int method = extras.getInt(ProcessorService.Extras.METHOD_EXTRA);
//                if (method== ServiceProvider.Methods.LOAD_COUNTRIES_METHOD)
//                    countryCursorLoader.forceLoad();
//                if (method== ServiceProvider.Methods.LOAD_CITIES_METHOD)
//                    cityCursorLoader.forceLoad();
                String message;
                if (success)
                    message = getResources().getString(R.string.loaded);
                else
                    message = getResources().getString(R.string.not_loaded);

                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, message, duration);
                toast.show();
            }
        }
    };
    private TextView txtCountryId;
    private ListView lvData;
    private ServiceHelper mServiceHelper;
    //TODO реализация с DbConnector
//    private DbConnector dbConnector;
//    private CountryCursorLoader countryCursorLoader;
//    private CityCursorLoader cityCursorLoader;
    private SimpleCursorAdapter scCitiesAdapter;
    private SimpleCursorAdapter scCountriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spCountry = (Spinner) findViewById(R.id.spinnerCountries);
        lvData = (ListView) findViewById(R.id.lvData);
//        dbConnector = new DbConnector(this);//TODO реализация с DbConnector
        txtCountryId = (TextView) spCountry.findViewById(R.id.country_id);
        if (txtCountryId != null && txtCountryId.getText() != null)
            countryId = Integer.parseInt(txtCountryId.getText().toString());
        // формируем столбцы сопоставления
        String[] from = new String[]{CountryEntry.COLUMN_COUNTRY_NAME, CountryEntry.COLUMN_VK_COUNTRY_ID};
        int[] to = new int[]{R.id.country_name, R.id.country_id};
        // создаем адаптер и настраиваем список
        scCountriesAdapter = new SimpleCursorAdapter(this, R.layout.spinner_item_country, null, from, to, 0);

        spCountry.setAdapter(scCountriesAdapter);
        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txtCountryId = (TextView) spCountry.findViewById(R.id.country_id);
                if (txtCountryId != null && txtCountryId.getText() != null)
                    countryId = Integer.parseInt(txtCountryId.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                countryId = -1;
            }
        });
        // формируем столбцы сопоставления
        from = new String[]{CityEntry.COLUMN_CITY_NAME};
        to = new int[]{R.id.tvText};
        // создаем адаптер и настраиваем список
        scCitiesAdapter = new SimpleCursorAdapter(this, R.layout.listview_item_city, null, from, to, 0);
        lvData.setAdapter(scCitiesAdapter);

        //TODO реализация с DbConnector
        // создаем лоадеры для чтения данных
//        countryCursorLoader = (CountryCursorLoader) getSupportLoaderManager().initLoader(COUNTRY_LOADER, null, this);
//        cityCursorLoader = (CityCursorLoader)getSupportLoaderManager().initLoader(CITY_LOADER, null, this);

        //TODO реализация с ContentProvider (CountryCityProvider)
        // создаем лоадеры для чтения данных
        getSupportLoaderManager().initLoader(COUNTRY_LOADER, null, this);
        getSupportLoaderManager().initLoader(CITY_LOADER, null, this);
        mServiceHelper = new ServiceHelper(this, BROADCAST_ACTION);
    }

    // обработка нажатия кнопки
    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoadCountries:
                mServiceHelper.loadCountries();
                break;
            case R.id.btnLoadCities:
                if (countryId > 0)
                    mServiceHelper.loadCities(countryId);
                break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mBroadcastReceiver, mFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
//        dbConnector.close();//TODO реализация с DbConnector
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        CursorLoader cursorLoader = null;
        String[] projection;
        switch (i) {
            case COUNTRY_LOADER:
//                cursorLoader = new CountryCursorLoader(this, dbConnector);//TODO реализация с DbConnector
                //TODO реализация с ContentProvider (CountryCityProvider)
                projection = new String[]{CountryEntry._ID, CountryEntry.COLUMN_COUNTRY_NAME, CountryEntry.COLUMN_VK_COUNTRY_ID};
                cursorLoader = new CountryCursorLoader(this, CountryEntry.COUNTRY_CONTENT_URI, projection, null, null, null);
                break;
            case CITY_LOADER:
//                cursorLoader = new CityCursorLoader(this, dbConnector);//TODO реализация с DbConnector
                //TODO реализация с ContentProvider (CountryCityProvider)
                projection = new String[]{CityEntry._ID, CityEntry.COLUMN_CITY_NAME};
                cursorLoader = new CityCursorLoader(this, CityEntry.CITY_CONTENT_URI, projection, null, null, null);
                break;
            default:
                break;
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (loader instanceof CountryCursorLoader)
            scCountriesAdapter.swapCursor(cursor);
        if (loader instanceof CityCursorLoader)
            scCitiesAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // освобождаем ресурсы
        if (loader instanceof CountryCursorLoader)
            scCountriesAdapter.swapCursor(null);
        if (loader instanceof CityCursorLoader)
            scCitiesAdapter.swapCursor(null);
    }

    static class CountryCursorLoader extends CursorLoader {
        //TODO реализация с DbConnector
        public CountryCursorLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
            super(context, uri, projection, selection, selectionArgs, sortOrder);
        }
        //TODO реализация с DbConnector
//        DbConnector dbConnector;
//        public CountryCursorLoader(Context context, DbConnector dbConnector) {
//            super(context);
//            this.dbConnector = dbConnector;
//        }
//        @Override
//        public Cursor loadInBackground() {
//            Cursor cursor;
//            cursor= dbConnector.getCountries();
//            return cursor;
//
//        }
    }

    static class CityCursorLoader extends CursorLoader {
        //TODO реализация с ContentProvider (CountryCityProvider)
        public CityCursorLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
            super(context, uri, projection, selection, selectionArgs, sortOrder);
        }

        @Override
        public Cursor loadInBackground() {
            String selection = CityEntry.COLUMN_VK_COUNTRY_CODE + " = " + countryId;
            setSelection(selection);
            return super.loadInBackground();
        }
        //TODO реализация с DbConnector
//        DbConnector dbConnector;
//        public CityCursorLoader(Context context, DbConnector dbConnector) {
//            super(context);
//            this.dbConnector = dbConnector;
//        }
//
//        @Override
//        public Cursor loadInBackground() {
//            Cursor cursor;
//            cursor= dbConnector.getCities(countryId);
//            return cursor;
//        }
    }
}

