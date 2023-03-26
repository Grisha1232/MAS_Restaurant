package org.example.Parsing;

import org.example.models.KitchenEquipment;
import org.example.models.KitchenEquipmentList;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParsingKitchenEquipmentList {
    public static ArrayList<KitchenEquipmentList> kitchenEquipmentLists;

    public static ArrayList<KitchenEquipmentList> getKitchenEquipmentLists(String jsonPath) {
        var json = new JSONObject(jsonPath);
        var arr = json.getJSONArray("equipment_type");
        kitchenEquipmentLists = new ArrayList<>();
        for (var i = 0; i < arr.length(); i++) {
            int equip_type_id = arr.getJSONObject(i).getInt("equip_type_id");
            String equip_type_name = arr.getJSONObject(i).getString("equip_type_name");
            kitchenEquipmentLists.add(new KitchenEquipmentList(equip_type_id, equip_type_name));
        }
        return kitchenEquipmentLists;
    }
}
