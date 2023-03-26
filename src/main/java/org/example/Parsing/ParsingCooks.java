package org.example.Parsing;

import org.example.models.Cooks;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ParsingCooks {
    public static ArrayList<Cooks> cooks;

    public static ArrayList<Cooks> getCooks(String jsonPath) {

        String content = null;
        try {
            content = Files.lines(Paths.get(jsonPath))
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        var json = new JSONObject(content);
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
