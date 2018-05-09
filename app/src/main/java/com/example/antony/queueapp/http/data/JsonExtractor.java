package com.example.antony.queueapp.http.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import java.lang.reflect.Type;

public class JsonExtractor {

    public final Gson gson;

    public JsonExtractor() {

        GsonBuilder builder = new GsonBuilder();

        JsonDeserializer<Period> pd = new JsonDeserializer<Period>() {
            @Override
            public Period deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return Period.parse(json.getAsString());
            }
        };

        JsonDeserializer<LocalDate> ldd = new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return LocalDate.parse(json.getAsString());
            }
        };

        JsonDeserializer<LocalTime> ltd = new JsonDeserializer<LocalTime>() {
            @Override
            public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return LocalTime.parse(json.getAsString());
            }
        };

        builder.registerTypeAdapter(Period.class, pd);
        builder.registerTypeAdapter(LocalDate.class, ldd);
        builder.registerTypeAdapter(LocalTime.class, ltd);
        gson = builder.create();
    }
}
