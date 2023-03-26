package org.example.Parsing;

import org.example.models.KitchenEquipment;
import org.example.models.Menu;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParsingEquipment {
    public static ArrayList<KitchenEquipment> equipments;
    public static ArrayList<KitchenEquipment> getDishesInMenu(String jsonPath) {
        var json = new JSONObject(jsonPath);
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
