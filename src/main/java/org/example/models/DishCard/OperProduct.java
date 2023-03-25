package org.example.models.DishCard;

import java.io.Serializable;

public class OperProduct implements Serializable {
    public int prod_type;
    public double prod_quantity;

    public OperProduct (int prod_type, double prod_quantity) {
        this.prod_quantity = prod_quantity;
        this.prod_type = prod_type;
    }
}
