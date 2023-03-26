package org.example.Parsing;

import org.example.models.Cooks;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParsingCooks {
    public static ArrayList<Cooks> cooks;

    public static ArrayList<Cooks> getCooks(String jsonPath) {
        var json = new JSONObject(jsonPath);
        JSONArray arr = json.getJSONArray("cookers");
        cooks = new ArrayList<>(arr.length());
        for (var i = 0; i < arr.length(); i++) {
            cooks.add(new Cooks(arr.getJSONObject(i).getInt("cook_id"),
                    arr.getJSONObject(i).getString("cook_name"),
                    arr.getJSONObject(i).getBoolean("cook_active")));
        }
        return cooks;
    }
}
