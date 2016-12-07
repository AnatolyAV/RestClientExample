package ru.andreev_av.restclientexample.objects;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class City {

    @JsonIgnore
    private int id;
    @JsonProperty(value = "title")
    private String cityName;
    @JsonProperty(value = "cid")
    private int vkCityId;
    @JsonIgnore
    private String vkCountryCode;

    public int getId() {
        return id;
    }

    public int getVkCityId() {
        return vkCityId;
    }

    public void setVkCityId(int vkCityId) {
        this.vkCityId = vkCityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getVkCountryCode() {
        return vkCountryCode;
    }

    public void setVkCountryCode(String vkCountryCode) {
        this.vkCountryCode = vkCountryCode;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", cityName, vkCountryCode);
    }

}
