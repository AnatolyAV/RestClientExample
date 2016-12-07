package ru.andreev_av.restclientexample.vk_ip;

public class VkHttpRequestLoadCoutries extends VkHttpRequest implements IHttpRequestForCountriesList {

    @Override
    public String perform() {
        url = getUrlForQueryingAllCountries();
        return performRequest();
    }
}
