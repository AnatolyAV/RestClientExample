package ru.andreev_av.restclientexample.processors;

import android.content.Context;

abstract class AbstractProcessor {
    //    DbConnector dbConnector;//TODO реализация с DbConnector
    private static final String FIELD_RESPONSE = "{\"response\":";
    Context mContext;

    StringBuilder correctJson(String sJson) {
        StringBuilder resultJson = new StringBuilder();
        if (sJson.contains(FIELD_RESPONSE)) {
            resultJson.append(sJson.replace(FIELD_RESPONSE, ""));
            resultJson.deleteCharAt(resultJson.lastIndexOf("}"));
        }
        return resultJson;
    }

}
