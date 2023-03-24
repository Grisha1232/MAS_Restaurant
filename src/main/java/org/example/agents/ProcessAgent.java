package org.example.agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.example.models.Cooks;
import org.example.models.KitchenEquipment;

import java.util.ArrayList;

public class ProcessAgent extends Agent {
    @Override
    protected void setup() {
        super.setup();
    }

    boolean allIsAvailable = true;

    private class ReserveEquipment extends Behaviour {
        @Override
        public void action() {
            // TODO: при многоповторке не особо понятно как будет работать ниже строчка.
            ACLMessage msg = myAgent.receive();
            if (msg != null) {
                try {
                   var machinesToReserve =(ArrayList<KitchenEquipment>)msg.getContentObject();
                    for (KitchenEquipment kitchenEquipment : machinesToReserve) {
                        if (!kitchenEquipment.equip_active) {
                            allIsAvailable = false;
                            break;
                        }
                    }
                    if (allIsAvailable) {
                        for (var i : machinesToReserve) {
                            i.equip_active = false;
                        }
                    }
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }
            } else {
                block();
            }
        }

        @Override
        public boolean done() {
            return allIsAvailable;
        }
    }
    private class ReserveCooks extends Behaviour {
        @Override
        public void action() {
            // TODO: при многоповторке не особо понятно как будет работать ниже строчка.
            ACLMessage msg = myAgent.receive();
            if (msg != null) {
                try {
                    var cooksToReserve =(ArrayList<Cooks>)msg.getContentObject();
                    for (Cooks i : cooksToReserve) {
                        if (!i.cook_active) {
                            allIsAvailable = false;
                            break;
                        }
                    }
                    if (allIsAvailable) {
                        for (var i : cooksToReserve) {
                            i.cook_active = false;
                        }
                    }
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }
            } else {
                block();
            }
        }

        @Override
        public boolean done() {
            return allIsAvailable;
        }
    }
}
