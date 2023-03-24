package org.example.models;

import java.io.Serializable;

public class Menu implements Serializable {
    public int menu_dish_id;
    public int menu_dish_card;
    public int menu_dish_price;
    public boolean menu_dish_active;

    public Menu(int menu_dish_id, int menu_dish_card, int menu_dish_price, boolean menu_dish_active) {
        this.menu_dish_id = menu_dish_id;
        this.menu_dish_card = menu_dish_card;
        this.menu_dish_price = menu_dish_price;
        this.menu_dish_active = menu_dish_active;
    }
}
