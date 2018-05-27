package ru.bmstu.queueapp.http;

import ru.bmstu.queueapp.http.data.Schedule;
import ru.bmstu.queueapp.http.data.SchedulesData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
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

        JsonSerializer<Period> ps = new JsonSerializer<Period>() {
            @Override
            public JsonElement serialize(Period src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.toString());
            }
        };

        JsonDeserializer<LocalDate> ldd = new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return LocalDate.parse(json.getAsString());
            }
        };

        JsonSerializer<LocalDate> lds = new JsonSerializer<LocalDate>() {
            @Override
            public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.toString());
            }
        };

        JsonDeserializer<LocalTime> ltd = new JsonDeserializer<LocalTime>() {
            @Override
            public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return LocalTime.parse(json.getAsString());
            }
        };

        JsonSerializer<LocalTime> lts = new JsonSerializer<LocalTime>() {
            @Override
            public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.toString());
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
        builder.registerTypeAdapter(Period.class, ps);
        builder.registerTypeAdapter(LocalDate.class, ldd);
        builder.registerTypeAdapter(LocalDate.class, lds);
        builder.registerTypeAdapter(LocalTime.class, ltd);
        builder.registerTypeAdapter(LocalTime.class, lts);
        builder.registerTypeAdapter(SchedulesData.class, sdd);
        gson = builder.create();
    }
}
