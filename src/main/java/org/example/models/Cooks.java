package org.example.models;

import java.io.Serializable;

public class Cooks implements Serializable {
    public int cook_id;
    public String cook_name;
    public boolean cook_active;
    public Cooks(int id, String cook_name, boolean cook_active) {
        cook_id = id;
        this.cook_name = cook_name;
        this.cook_active = cook_active;
    }
}
