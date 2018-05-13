package com.example.antony.queueapp.http;

import com.example.antony.queueapp.http.data.Schedule;
import com.example.antony.queueapp.http.data.SchedulesData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import java.lang.reflect.Type;
import java.util.ArrayList;

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

        JsonDeserializer<SchedulesData> sdd = new JsonDeserializer<SchedulesData>() {
            @Override
            public SchedulesData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jo = json.getAsJsonObject();
                Period period = context.deserialize(jo.get("period"), Period.class);
                ArrayList<Schedule> schedules = context.deserialize(jo.get("schedules"), new TypeToken<ArrayList<Schedule>>(){}.getType());
                return new SchedulesData(period, schedules);
            }
        };

        builder.registerTypeAdapter(Period.class, pd);
        builder.registerTypeAdapter(LocalDate.class, ldd);
        builder.registerTypeAdapter(LocalTime.class, ltd);
        builder.registerTypeAdapter(SchedulesData.class, sdd);
        gson = builder.create();
    }
}
