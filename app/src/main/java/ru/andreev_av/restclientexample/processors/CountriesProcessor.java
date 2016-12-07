package ru.andreev_av.restclientexample.processors;

import android.content.ContentValues;
import android.content.Context;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.Collection;

import ru.andreev_av.restclientexample.data.DbData.CountryEntry;
import ru.andreev_av.restclientexample.objects.Country;
import ru.andreev_av.restclientexample.vk_ip.IHttpRequestForCountriesList;
import ru.andreev_av.restclientexample.vk_ip.VkHttpRequestLoadCoutries;

public class CountriesProcessor extends AbstractProcessor {

    private IHttpRequestForCountriesList httpRequestLoadCountries = new VkHttpRequestLoadCoutries();

    public CountriesProcessor(Context context) {
        mContext = context;
    }

    public boolean loadCountries() {
        try {
            String tempJson = httpRequestLoadCountries.perform();
            Collection<Country> list = null;
            if (tempJson.length() > 0 && !tempJson.isEmpty()) {
                StringBuilder resultJson = correctJson(tempJson);
                list = new ObjectMapper().readValue(resultJson.toString(), new TypeReference<Collection<Country>>() {
                });
            }
            if (list != null) {
                fillDb(list);// заносим данные в БД
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    private void fillDb(Collection<Country> list) {
        //TODO реализация с DbConnector
//        dbConnector = new DbConnector(mContext);
//        dbConnector.loadCountries(list);
//        dbConnector.close();
        //TODO реализация с ContentProvider (CountryCityProvider)
        ContentValues cv = new ContentValues();
        for (Country country : list) {
            cv.put(CountryEntry.COLUMN_COUNTRY_NAME, country.getCountryName());
            cv.put(CountryEntry.COLUMN_VK_COUNTRY_ID, country.getVkCountryId());
            String selection = CountryEntry.COLUMN_VK_COUNTRY_ID + " = ?";
            String[] selectionArgs = new String[]{String.valueOf(country.getVkCountryId())};
            int updCount = mContext.getContentResolver().update(CountryEntry.COUNTRY_CONTENT_URI, cv, selection, selectionArgs);
            if (updCount == 0) {
                mContext.getContentResolver().insert(CountryEntry.COUNTRY_CONTENT_URI, cv);
            }
        }
    }
}
