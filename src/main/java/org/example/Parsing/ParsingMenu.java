package org.example.Parsing;

import org.example.models.Menu;
import org.example.models.Storage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class ParsingMenu {
    public static ArrayList<Menu> dishesInMenu;

    public static ArrayList<Menu> getDishesInMenu(String jsonPath) {
        var json = new JSONObject(jsonPath);
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
