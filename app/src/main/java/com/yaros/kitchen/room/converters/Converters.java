package com.yaros.kitchen.room.converters;

import androidx.room.TypeConverter;

 import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yaros.kitchen.models.KitchenItemModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Converters {
    @TypeConverter
    public static List<Integer> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<Integer> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public static KitchenItemModel fromAccount(String value) {
        Type listType = new TypeToken<KitchenItemModel>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayListDouble(KitchenItemModel list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}