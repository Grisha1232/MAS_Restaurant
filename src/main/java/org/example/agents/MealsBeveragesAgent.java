package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.util.Deque;

public class MealsBeveragesAgent extends Agent {
    Integer ordDish;
    Deque<AID> operations;
    private jade.wrapper.AgentContainer mainContainer;

    @Override
    protected void setup() {
        var args = getArguments();
        this.ordDish = (Integer) args[1];
        // TODO: сделать запрос StorageArgent о операциях и названии блюда.
        addBehaviour(new CookMeal());
    }

    private class CookMeal extends Behaviour {
        boolean isDone = false;
        @Override
        public void action() {
            if (!operations.isEmpty()) {
                var operation = operations.getFirst();
                var msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(operation);
                msg.setContent("start");
                send(msg);
            } else {
                isDone = true;
            }
        }

        @Override
        public boolean done() {
            return isDone;
        }
    }
}
