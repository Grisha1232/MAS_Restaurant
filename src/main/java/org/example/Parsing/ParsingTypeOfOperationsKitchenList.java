package org.example.Parsing;

import org.example.models.TypeOfOperationsKitchenList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParsingTypeOfOperationsKitchenList {
    public static ArrayList<TypeOfOperationsKitchenList> typeOfOperationsKitchenLists;

    public static ArrayList<TypeOfOperationsKitchenList> getTypeOfOperationsKitchenLists(String jsonPath) {
        var json = new JSONObject(jsonPath);
        JSONArray arr = json.getJSONArray("menu_dishes");
        typeOfOperationsKitchenLists = new ArrayList<>(arr.length());
        for (var i = 0; i < arr.length(); i++) {
            int oper_type_id = arr.getJSONObject(i).getInt("oper_type_id");
            String oper_type_name = arr.getJSONObject(i).getString("oper_type_name");
            typeOfOperationsKitchenLists.add(new TypeOfOperationsKitchenList(oper_type_id,oper_type_name));
        }
        return typeOfOperationsKitchenLists;
    }
}
