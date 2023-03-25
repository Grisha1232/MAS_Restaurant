package org.example.models.Visitor;

import java.io.Serializable;

public class VisOrdDishes implements Serializable {
    public int ord_dish_id;
    public int menu_dish;

    public VisOrdDishes(int ord_dish_id, int menu_dish) {
        this.ord_dish_id = ord_dish_id;
        this.menu_dish = menu_dish;
    }
}
