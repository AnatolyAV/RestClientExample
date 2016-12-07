package ru.andreev_av.restclientexample.objects;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {

    @JsonIgnore
    private int id;
    @JsonProperty(value = "title")
    private String countryName;
    @JsonProperty(value = "cid")
    private int vkCountryId;

    public int getId() {
        return id;
    }

    public int getVkCountryId() {
        return vkCountryId;
    }

    public void setVkCountryId(int vkCountryId) {
        this.vkCountryId = vkCountryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }


    @Override
    public String toString() {
        return String.format("%s", countryName);
    }
}
