package org.example.Parsing;

import org.example.models.Visitor.VisOrdDishes;
import org.example.models.Visitor.Visitor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class ParsingVisitor {
    public static ArrayList<Visitor> visitors;

    public static ArrayList<Visitor> getVisitors(String jsonPath) throws ParseException {
        var json = new JSONObject(jsonPath);
        JSONArray arr = json.getJSONArray( "visitors_orders");
        visitors = new ArrayList<>(arr.length());
        for (var i = 0; i < arr.length(); i++) {
            String vis_name = arr.getJSONObject(i).getString("vis_name");
            Date vis_ord_started = ParseData.ParseData(arr.getJSONObject(i).getString("vis_ord_started"));
            Date vis_ord_ended = ParseData.ParseData(arr.getJSONObject(i).getString("vis_ord_ended"));
            int vis_ord_total = arr.getJSONObject(i).getInt("vis_ord_total");
            ArrayList<VisOrdDishes> vis_ord_dishes = new ArrayList<>();
            var arr1 = arr.getJSONArray(i);
            for (var j = 0; j < arr1.length(); j++)  {
                vis_ord_dishes.add(new VisOrdDishes(arr.getJSONObject(j).getInt("ord_dish_id"),
                        arr.getJSONObject(j).getInt("menu_dish")));
            }
            visitors.add(new Visitor(vis_name, vis_ord_started,vis_ord_ended, vis_ord_total, vis_ord_dishes));

        }
        return visitors;
    }
}
