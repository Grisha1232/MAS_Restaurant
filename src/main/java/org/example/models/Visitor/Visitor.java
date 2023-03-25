package org.example.models.Visitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Visitor implements Serializable {
    public String vis_name;
    public Date vis_ord_started;
    public Date vis_ord_ended;
    public int vis_ord_total;
    public ArrayList<VisOrdDishes> vis_ord_dishes;

    public Visitor(String vis_name, Date vis_ord_started, Date vis_ord_ended,
                   int vis_ord_total, ArrayList<VisOrdDishes> vis_ord_dishes) {
        this.vis_name = vis_name;
        this.vis_ord_started = vis_ord_started;
        this.vis_ord_ended = vis_ord_ended;
        this.vis_ord_total = vis_ord_total;
        this.vis_ord_dishes = vis_ord_dishes;
    }
}
