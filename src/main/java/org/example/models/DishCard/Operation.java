package org.example.models.DishCard;

import java.io.Serializable;
import java.util.ArrayList;

public class Operation implements Serializable {
    public int oper_type;
    public int equip_type;
    public double oper_time;
    public int oper_async_point;
    public ArrayList<OperProduct> oper_products;


    public Operation(int oper_type, int equip_type,double oper_time, int oper_async_point, ArrayList<OperProduct> oper_products) {
        this.oper_type = oper_type;
        this.equip_type = equip_type;
        this.oper_time = oper_time;
        this.oper_async_point = oper_async_point;
        this.oper_products = oper_products;
    }
}
