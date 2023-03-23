package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.util.Deque;

public class MealsBeveragesAgent extends Agent {
    String mealName;
    Integer ordDish;
    Integer processorID;
    Deque<AID> operations;

    @Override
    protected void setup() {
        var args = getArguments();
        this.mealName = (String) args[0];
        this.ordDish = (Integer) args[1];
        this.processorID = (Integer) args[2];
        this.operations = (Deque<AID>) args[3];
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
