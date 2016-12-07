package ru.andreev_av.restclientexample.vk_ip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class VkHttpRequest {
    String url;

    public String performRequest() {
        InputStream content = null;
        BufferedReader in = null;
        StringBuilder sbJson = new StringBuilder();
        try {
            URL fullUrl = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) fullUrl.openConnection();
            connection.setRequestMethod("GET");
            content = connection.getInputStream();
            in = new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = in.readLine()) != null) {
                sbJson.append(line);
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (content != null)
                    content.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return sbJson.toString();
    }

    protected String getUrlForQueryingAllCountries() {
        return String.format("%sdatabase.getCountries", VkApiData.BASE_URL);
    }

    protected String getUrlForQueryingCitiesByCountryId(int countryId) {
        return String.format("%sdatabase.getCities&country_id=%s", VkApiData.BASE_URL, countryId);
    }

}
