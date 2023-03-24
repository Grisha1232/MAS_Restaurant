package org.example.models;

import java.io.Serializable;

public class StorageList implements Serializable {
    public int prod_type_id;
    public String prod_type_name;
    public boolean prod_is_food;

    public StorageList(int id, String name, boolean isFood) {
        prod_type_id = id;
        prod_type_name = name;
        prod_is_food = isFood;
    }

}
