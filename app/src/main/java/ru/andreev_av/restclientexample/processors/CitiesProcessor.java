package ru.andreev_av.restclientexample.processors;

import android.content.ContentValues;
import android.content.Context;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.Collection;

import ru.andreev_av.restclientexample.data.DbData.CityEntry;
import ru.andreev_av.restclientexample.objects.City;
import ru.andreev_av.restclientexample.vk_ip.IHttpRequestForCitiesList;
import ru.andreev_av.restclientexample.vk_ip.VkHttpRequestLoadCitiesForCountryId;

public class CitiesProcessor extends AbstractProcessor {

    private IHttpRequestForCitiesList httpRequestLoadCities = new VkHttpRequestLoadCitiesForCountryId();

    public CitiesProcessor(Context context) {
        mContext = context;
    }

    public boolean loadCities(int countryId) {
        try {
            String tempJson = httpRequestLoadCities.perform(countryId);
            Collection<City> list = null;
            if (tempJson.length() > 0 && !tempJson.isEmpty()) {
                StringBuilder resultJson = correctJson(tempJson);
                list = new ObjectMapper().readValue(resultJson.toString(), new TypeReference<Collection<City>>() {
                });
            }
            if (list != null) {
                fillDb(list, countryId);// заносим данные в БД
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    private void fillDb(Collection<City> list, int countryId) {
        //TODO реализация с DbConnector
//        dbConnector = new DbConnector(mContext);
//        dbConnector.loadCities(list, countryId);
//        dbConnector.close();
        //TODO реализация с ContentProvider (CountryCityProvider)
        ContentValues cv = new ContentValues();
        for (City city : list) {
            cv.put(CityEntry.COLUMN_CITY_NAME, city.getCityName());
            cv.put(CityEntry.COLUMN_VK_CITY_ID, city.getVkCityId());
            cv.put(CityEntry.COLUMN_VK_COUNTRY_CODE, countryId);
            String selection = CityEntry.COLUMN_VK_CITY_ID + " = ?";
            String[] selectionArgs = new String[]{String.valueOf(city.getVkCityId()),};
            int updCount = mContext.getContentResolver().update(CityEntry.CITY_CONTENT_URI, cv, selection, selectionArgs);
            if (updCount == 0) {
                mContext.getContentResolver().insert(CityEntry.CITY_CONTENT_URI, cv);
            }
        }

    }
}
