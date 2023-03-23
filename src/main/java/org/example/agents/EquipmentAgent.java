package org.example.agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;

public class EquipmentAgent extends Agent {
    Integer equipmentType;
    String equipmentName;
    Boolean isEquipmentActive = false;
    @Override
    protected void setup() {
        var args = getArguments();
        this.equipmentType = (Integer) args[0];
        this.equipmentName = (String) args[1];
        this.isEquipmentActive = (Boolean) args[2];

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                addBehaviour(new ReserveEquipment());
            }
        });
    }

    private class ReserveEquipment extends Behaviour {

        @Override
        public void action() {
            var msg = myAgent.receive();
            if (msg != null) {
                isEquipmentActive = msg.getContent().equals("reserve");
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
