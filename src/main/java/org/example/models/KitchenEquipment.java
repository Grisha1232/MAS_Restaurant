package org.example.models;

import java.io.Serializable;

public class KitchenEquipment implements Serializable {
    public int equip_type;
    public String equip_name;
    public boolean equip_active;

    KitchenEquipment(int type, String name, boolean active) {
        equip_type = type;
        equip_name = name;
        equip_active = active;
    }

}
