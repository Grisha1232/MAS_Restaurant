package org.example.Parsing;

import org.example.models.KitchenEquipment;
import org.example.models.Menu;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ParsingEquipment {
    public static ArrayList<KitchenEquipment> equipments;
    public static ArrayList<KitchenEquipment> getDishesInMenu(String jsonPath) {
        String content = null;
        try {
            content = Files.lines(Paths.get(jsonPath))
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        var json = new JSONObject(content);
        JSONArray arr = json.getJSONArray("equipment");
        equipments = new ArrayList<>(arr.length());
        for (var i = 0; i < arr.length(); i++) {
            equipments.add(new KitchenEquipment(arr.getJSONObject(i).getInt("equip_id"),
                    arr.getJSONObject(i).getInt("equip_type"),
                    arr.getJSONObject(i).getString("equip_name"),
                    arr.getJSONObject(i).getBoolean("equip_active")));
        }
        return equipments;
    }
}
