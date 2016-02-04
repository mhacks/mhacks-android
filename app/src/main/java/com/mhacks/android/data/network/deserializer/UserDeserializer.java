package com.mhacks.android.data.network.deserializer;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
        JsonObject data = json.getAsJsonObject().get("data").getAsJsonObject();

        User user = new User();
        user.setId(data.get("id").getAsInt());
        user.setEmail(data.get("email").getAsString());
        user.setFirstName(data.get("first_name").getAsString());
        user.setLastName(data.get("last_name").getAsString());
        user.setUid(data.get("uid").getAsString());
        user.setRoles(data.get("roles").getAsInt());

        Log.d(TAG, user.firstName);
        return user;
    }
}
