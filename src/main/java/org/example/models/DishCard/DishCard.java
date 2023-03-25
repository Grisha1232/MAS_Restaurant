package org.example.models.DishCard;


import java.io.Serializable;
import java.util.ArrayList;

public class DishCard implements Serializable {
    public int card_id;
    public String dish_name;
    public String card_descr;
    public double card_time;
    public ArrayList<Operation> operations;

    public DishCard(int card_id, String dish_name, String card_descr, double card_time, ArrayList<Operation> operations) {
        this.card_id = card_id;
        this.dish_name = dish_name;
        this.card_descr = card_descr;
        this.card_time = card_time;
        this.operations = operations;
    }
}