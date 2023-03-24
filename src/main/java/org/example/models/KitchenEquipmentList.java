package org.example.models;

import java.io.Serializable;

public class KitchenEquipmentList implements Serializable {
    public int equip_type_id;
    public String equip_type_name;

    public KitchenEquipmentList(int id, String name ) {
        equip_type_name = name;
        equip_type_id = id;
    }
}
