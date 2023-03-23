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

    private class ReserveForOperation extends Behaviour {

        @Override
        public void action() {
            isEquipmentActive = !isEquipmentActive;
        }

        @Override
        public boolean done() {
            return true;
        }
    }

}
