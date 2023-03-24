package org.example.models;

import java.io.Serializable;

public class TypeOfOperationsKitchenList implements Serializable {
    public int oper_type_id;
    public String oper_type_name;

    public TypeOfOperationsKitchenList(int id, String oper_type_name) {
        oper_type_id = id;
        this.oper_type_name = oper_type_name;
    }

}
