package org.example.models;

import java.io.Serializable;

public class KitchenEquipment implements Serializable {
    public int equip_id;
    public int equip_type;
    public String equip_name;
    public boolean equip_active;

    public KitchenEquipment(int equip_id,int type, String name, boolean active) {
        this.equip_id = equip_id;
        equip_type = type;
        equip_name = name;
        equip_active = active;
    }

}
