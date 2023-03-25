package org.example.models;

import java.io.Serializable;
import java.util.Date;

public class Process implements Serializable {

    public int oper_id;
    public int oper_proc;
    public int oper_card;
    public Date oper_started;
    public Date oper_ended;
    public int oper_equip_id;
    public int oper_coocker_id;
    public boolean oper_active;
    public double oper_time;

    public Process(int oper_id, int oper_proc, int oper_card, Date oper_started, Date oper_ended, int oper_equip_id,
                   int oper_coocker_id, boolean oper_active, double oper_time) {
        this.oper_active = oper_active;
        this.oper_equip_id = oper_equip_id;
        this.oper_card = oper_card;
        this.oper_coocker_id = oper_coocker_id;
        this.oper_id = oper_id;
        this.oper_proc = oper_proc;
        this.oper_started = oper_started;
        this.oper_ended = oper_ended;
        this.oper_time = oper_time;
    }

}
