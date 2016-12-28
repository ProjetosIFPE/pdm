package br.edu.ifpe.tads.pdm.projeto.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Edmilson on 27/11/2016.
 */

public class PreferencesUtil {
    private static final String APP_CACHE = "br.edu.ifpe.tads.pdm.projeto.util.STORAGE";

    public static <T> void putList(Context context, String key, List<T> value) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.apply();

    }

    public static <T> ArrayList<T> getList(Class<T[]> klass, Context context, String key) {
        SharedPreferences preferences = getSharedPreferences(context);
        Gson gson = new Gson();
        String json = preferences.getString(key, "");

        return new ArrayList<T>(Arrays.asList(gson.fromJson(json, klass)));
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getInt(key, -1);
    }

    public static void clearCachedData(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_CACHE, Context.MODE_PRIVATE);
    }
}
