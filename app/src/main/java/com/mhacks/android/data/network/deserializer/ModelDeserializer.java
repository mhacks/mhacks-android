package com.mhacks.android.data.network.deserializer;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Omkar Moghe on 1/31/2016.
 */
public class ModelDeserializer<T> implements JsonDeserializer<T> {
    public static final String TAG = "ModelDeserializer";

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonElement results = json.getAsJsonObject().get("results");
        //Log.d(TAG, results.getAsJsonArray().getAsString());
        return new Gson().fromJson(results.toString(), typeOfT);
    }
}
