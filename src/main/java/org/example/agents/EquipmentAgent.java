package org.example.agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class EquipmentAgent extends Agent {
    Integer equipmentType;
    String equipmentName;
    Boolean isEquipmentActive = false;
    @Override
    protected void setup() {
        // TODO: type, name and active from args
        // getArguments()
    }

    private class ReserveEquipment extends Behaviour {

        @Override
        public void action() {
            var msg = myAgent.receive();
            if (msg != null) {
                if (msg.getContent().equals("reserve")) {
                    isEquipmentActive = true;
                } else {
                    isEquipmentActive = false;
                }
            } else {
                block();
            }
        }

        @Override
        public boolean done() {
            return false;
        }
    }

}
