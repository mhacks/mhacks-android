package com.mhacks.android.data.network.deserializer;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mhacks.android.data.model.User;

import java.lang.reflect.Type;

/**
 * Created by Omkar Moghe on 1/31/2016.
 *
 * user data is returned in a JSON object called "data" so it needs its own deserializer
 */
public class UserDeserializer implements JsonDeserializer<User> {
    public static final String TAG = "UserDeserializer";

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonElement data = json.getAsJsonObject().get("data");
        Log.d(TAG, "DESERIALIZING");
        Log.d(TAG, data.toString());
        User obj = new Gson().fromJson(data.toString(), User.class);
        Log.d(TAG, obj.firstName);
        return obj;
    }
}
