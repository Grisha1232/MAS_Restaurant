package org.example.models;

import java.io.Serializable;
import java.util.Date;

public class Storage implements Serializable {
    public int prod_item_id;
    public int prod_item_type;
    public String prod_item_name;
    public String prod_item_company;
    public String prod_item_unit;
    public double prod_item_quantity;
    public double prod_item_cost;
    public Date prod_item_delivered;
    public Date prod_item_valid_until;

    public Storage(int id, int type, String name, String company, String unit, double quantity, double cost, Date delivered, Date valid) {
        prod_item_id = id;
        prod_item_type = type;
        prod_item_name = name;
        prod_item_company = company;
        prod_item_unit = unit;
        prod_item_quantity = quantity;
        prod_item_cost = cost;
        prod_item_delivered = delivered;
        prod_item_valid_until = valid;
    }

}
