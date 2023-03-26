package org.example.Parsing;

import org.example.models.Storage;
import org.json.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class ParsingStorage {
    public static ArrayList<Storage> storage;

    public static ArrayList<Storage> getStorageModelList(String jsonPath) throws ParseException {
        String content = null;
        try {
            content = Files.lines(Paths.get(jsonPath))
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        var json = new JSONObject(content);

        JSONArray arr = json.getJSONArray("products");
        storage = new ArrayList<>(arr.length());
        for (var i = 0; i < arr.length(); i++) {
            storage.add(new Storage(arr.getJSONObject(i).getInt("prod_item_id"),
                    arr.getJSONObject(i).getInt("prod_item_type"),
                    arr.getJSONObject(i).getString("prod_item_name"),
                    arr.getJSONObject(i).getString("prod_item_company"),
                    arr.getJSONObject(i).getString("prod_item_unit"),
                    arr.getJSONObject(i).getDouble("prod_item_quantity"),
                    arr.getJSONObject(i).getDouble("prod_item_cost"),
                    ParseData.ParseData(arr.getJSONObject(i).getString("prod_item_delivered")),
                    ParseData.ParseData(arr.getJSONObject(i).getString("prod_item_valid_until"))
            ));
        }
        return storage;
    }
}
