package ru.andreev_av.restclientexample.vk_ip;

public class VkHttpRequestLoadCitiesForCountryId extends VkHttpRequest implements IHttpRequestForCitiesList {


    @Override
    public String perform(int countryId) {
        url = getUrlForQueryingCitiesByCountryId(countryId);
        return performRequest();
    }
}
