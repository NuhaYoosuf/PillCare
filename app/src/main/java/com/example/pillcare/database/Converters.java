package com.example.pillcare.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class Converters {
    private static Gson gson = new Gson();
    
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }
    
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
    
    @TypeConverter
    public static List<String> fromString(String value) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(value, listType);
    }
    
    @TypeConverter
    public static String fromStringList(List<String> list) {
        return gson.toJson(list);
    }
}