package org.example.agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class OperationAgent extends Agent {
    @Override
    protected void setup() {
        super.setup();
    }

    private class GetDishTransferToCooksEquipment extends Behaviour {

        @Override
        public void action() {
            var dish = myAgent.receive();
            if (dish != null) {
                //TODO: распарсить блюдо в список операций и список поворов
            }
        }

        @Override
        public boolean done() {
            return false;
        }
    }
    private class SendEquipmentToProcessAgent {
        // TODO: дописать отправку снаряжения процессу

    }
    private class SendCooksToProcessAgent {
        // TODO: дописать отправку поваров процессу.
    }
}
