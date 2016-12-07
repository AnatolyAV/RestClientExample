package ru.andreev_av.restclientexample.providers;

import android.content.Context;
import android.os.Bundle;

import ru.andreev_av.restclientexample.processors.CitiesProcessor;
import ru.andreev_av.restclientexample.processors.CountriesProcessor;

public class ServiceProvider implements IServiceProvider {
    private final Context mContext;

    public ServiceProvider(Context context) {
        mContext = context;
    }

    @Override
    public boolean RunTask(int methodId, Bundle extras) {
        switch (methodId) {
            case Methods.LOAD_CITIES_METHOD:
                return loadCities(extras);
            case Methods.LOAD_COUNTRIES_METHOD:
                return loadCountries();
        }
        return false;
    }

    private boolean loadCountries() {
        return new CountriesProcessor(mContext).loadCountries();
    }

    private boolean loadCities(Bundle extras) {
        int countryId = extras.getInt(Methods.PARAMETER_COUNTRY_ID);
        return new CitiesProcessor(mContext).loadCities(countryId);
    }

    public static class Methods {
        public static final int LOAD_COUNTRIES_METHOD = 1;
        public static final int LOAD_CITIES_METHOD = 2;
        public static final String PARAMETER_COUNTRY_ID = "country_id";
    }

}
