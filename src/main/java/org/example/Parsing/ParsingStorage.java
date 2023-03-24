package org.example.Parsing;

import org.example.models.Storage;
import org.json.*;

import java.util.ArrayList;
import java.util.Date;

public class ParsingStorage {
    public static ArrayList<Storage> getStorageModelList(String jsonPath) {
        var json = new JSONObject(jsonPath);

        JSONArray arr = json.getJSONArray("products");
        ArrayList<Storage> storage = new ArrayList<>(arr.length());
        for (var i = 0; i < arr.length(); i++) {
            storage.add(new Storage(arr.getJSONObject(i).getInt("prod_item_id"),
                    arr.getJSONObject(i).getInt("prod_item_type"),
                    arr.getJSONObject(i).getString("prod_item_name"),
                    arr.getJSONObject(i).getString("prod_item_company"),
                    arr.getJSONObject(i).getString("prod_item_unit"),
                    arr.getJSONObject(i).getDouble("prod_item_quantity"),
                    arr.getJSONObject(i).getDouble("prod_item_cost"),
                    new Date(), new Date()
            ));
        }
        return storage;
    }
}
