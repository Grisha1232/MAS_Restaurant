package org.example.Parsing;

import org.example.models.Menu;
import org.example.models.Storage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class ParsingMenu {
    public static ArrayList<Menu> dishesInMenu;

    public static ArrayList<Menu> getDishesInMenu(String jsonPath) {
        String content = null;
        try {
            content = Files.lines(Paths.get(jsonPath))
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        var json = new JSONObject(content);
        JSONArray arr = json.getJSONArray("menu_dishes");
        dishesInMenu = new ArrayList<>(arr.length());
        for (var i = 0; i < arr.length(); i++) {
            dishesInMenu.add(new Menu(arr.getJSONObject(i).getInt("menu_dish_id"),
                    arr.getJSONObject(i).getInt("menu_dish_card"),
                    arr.getJSONObject(i).getInt("menu_dish_price"),
                    arr.getJSONObject(i).getBoolean("menu_dish_active")));
        }
        return dishesInMenu;
    }
}
