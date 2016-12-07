package ru.andreev_av.restclientexample.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class DbData {

    // Content Authority
    public static final String CONTENT_AUTHORITY = "ru.andreev_av.restclientexample.vkdata";
    // Создаём объект Uri
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private DbData() {
    }

    public static final class CountryEntry implements BaseColumns {
        public static final String COUNTRY_PATH = "country";
        public static final Uri COUNTRY_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, COUNTRY_PATH);
        public static final String COUNTRY_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
                + CONTENT_AUTHORITY + "." + COUNTRY_PATH;

        public static final String DB_TABLE_COUNTRY = "country";
        public static final String COLUMN_COUNTRY_NAME = "countryName";
        public static final String COLUMN_VK_COUNTRY_ID = "vkCountryId";
    }

    public static final class CityEntry implements BaseColumns {
        public static final String CITY_PATH = "city";
        public static final Uri CITY_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, CITY_PATH);
        public static final String CITY_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
                + CONTENT_AUTHORITY + "." + CITY_PATH;

        public static final String DB_TABLE_CITY = "city";
        public static final String COLUMN_CITY_NAME = "cityName";
        public static final String COLUMN_VK_CITY_ID = "vkCityId";
        public static final String COLUMN_VK_COUNTRY_CODE = "vkCountryCode";
    }
}
