package org.example.Parsing;

import org.example.models.StorageList;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParsingStorageList {
    public static ArrayList<StorageList> storageList;

    public static ArrayList<StorageList> getStorageList(String jsonPath) {
        var json = new JSONObject(jsonPath);
        var arr = json.getJSONArray("product_types");
        storageList = new ArrayList<>();
        for (var i = 0; i < arr.length(); i++) {
            int prod_type_id = arr.getJSONObject(i).getInt("prod_type_id");
            String prod_type_name = arr.getJSONObject(i).getString( "prod_type_name");
            boolean prod_is_food = arr.getJSONObject(i).getBoolean("prod_is_food");
            storageList.add(new StorageList(prod_type_id,prod_type_name,prod_is_food));
        }
        return storageList;
    }
}
