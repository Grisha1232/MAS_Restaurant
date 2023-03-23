package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.Deque;

public class OrderAgent extends Agent {
    // Contains mealsBeveragesAgents
    Deque<AID> meals;
    Integer orderID;

    @Override
    protected void setup() {
        var args = getArguments();
        this.meals = (Deque<AID>) args[0];
        this.orderID = (Integer) args[1];
        // TODO: addBehaviour to agent
    }

    private class ReceiveOrder extends CyclicBehaviour {

        @Override
        public void action() {
            var msg = myAgent.receive();
            if (msg != null) {
                try {
                    meals = (Deque<AID>) msg.getContentObject();
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }
                if (meals != null) {
                    var messageToMealsAgent = new ACLMessage(ACLMessage.INFORM);
                    // TODO: send message to StorageAgent about product we have and reserve them if have
                    // TODO: cancel if we do not have these products

                }
            } else {
                block();
            }
        }
    }

    private class NotifyAboutTime extends Behaviour {
        Integer step = 0;
        @Override
        public void action() {
            // TODO: send message to ask time left from operationAgent (step = 0)
            // TODO: receive message from operationAgent about time left (step = 1)
            // TODO: send message about time left to VisitorAgent (step = 2)
        }

        @Override
        public boolean done() {
            return step == 3;
        }
    }


}
