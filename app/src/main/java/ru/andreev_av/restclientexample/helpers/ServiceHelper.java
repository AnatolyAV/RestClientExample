package ru.andreev_av.restclientexample.helpers;

import android.content.Context;
import android.os.Bundle;

import ru.andreev_av.restclientexample.providers.ServiceProvider;
import ru.andreev_av.restclientexample.services.ProcessorService;

public class ServiceHelper extends AbstractServiceHelper {

    public ServiceHelper(Context context, String resultAction) {
        super(context, ProcessorService.Providers.ROWS_PROVIDER, resultAction);
    }

    public void loadCountries() {
        RunMethod(ServiceProvider.Methods.LOAD_COUNTRIES_METHOD);
    }

    public void loadCities(int countryId) {
        Bundle extras = new Bundle();

        extras.putInt(ServiceProvider.Methods.PARAMETER_COUNTRY_ID, countryId);

        RunMethod(ServiceProvider.Methods.LOAD_CITIES_METHOD, extras);
    }
}
