package org.example.Parsing;

import org.example.models.DishCard.DishCard;
import org.example.models.DishCard.OperProduct;
import org.example.models.DishCard.Operation;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ParsingDishCard {
    public static ArrayList<DishCard> dishCards;

    public static ArrayList<DishCard> getDishCards(String jsonPath) {
        String content = null;
        try {
            content = Files.lines(Paths.get(jsonPath))
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        var json = new JSONObject(content);
        JSONArray arr = json.getJSONArray("dish_cards");
        dishCards = new ArrayList<>(arr.length());
        for (var i = 0; i < arr.length(); i++) {
            int card_id = arr.getJSONObject(i).getInt("card_id");
            String dish_name = arr.getJSONObject(i).getString("dish_name");
            String card_descr = arr.getJSONObject(i).getString("card_descr");
            double card_time = arr.getJSONObject(i).getDouble("card_time");
            var operation = new ArrayList<Operation>();
            var arr1 = arr.getJSONObject(i).getJSONArray("operations");
            for (var j = 0; j < arr1.length(); j++) {
                int oper_type = arr1.getJSONObject(j).getInt("oper_type");
                int equip_type = arr1.getJSONObject(j).getInt("equip_type");
                double oper_time = arr1.getJSONObject(j).getDouble("oper_time");
                int oper_async_point = arr1.getJSONObject(j).getInt("oper_async_point");
                var operProducts = new ArrayList<OperProduct>();
                var arr2 = arr1.getJSONObject(j).getJSONArray("oper_products");
                for (var x = 0; x < arr2.length(); x++) {
                    int prod_type = arr2.getJSONObject(x).getInt("prod_type");
                    double prod_quantity = arr2.getJSONObject(x).getDouble("prod_quantity");
                    operProducts.add(new OperProduct(prod_type,prod_quantity));
                }
                operation.add(new Operation(oper_type,equip_type,oper_time,oper_async_point,operProducts));
            }
            dishCards.add(new DishCard(card_id,dish_name,card_descr,card_time,operation));
        }
        return dishCards;
    }
}
