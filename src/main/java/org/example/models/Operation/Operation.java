package org.example.models.Operation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Operation implements Serializable {

    public int proc_id;
    public int ord_dish;
    public Date proc_started;
    public Date proc_ended;
    public boolean proc_active;
    public ArrayList<OperProc> proc_operations;


    public Operation(int proc_id, int ord_dish, Date proc_started,
                     Date proc_ended, boolean proc_active, ArrayList<OperProc> proc_operations) {
        this.proc_id = proc_id;
        this.ord_dish = ord_dish;
        this.proc_started = proc_started;
        this.proc_ended = proc_ended;
        this.proc_active = proc_active;
        this.proc_operations = proc_operations;
    }
}
