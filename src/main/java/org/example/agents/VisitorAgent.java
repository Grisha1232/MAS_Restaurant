package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.example.Pair;

import java.io.IOException;
import java.util.ArrayList;


public class VisitorAgent extends Agent {
    String name;
    int orderTotal;
    ArrayList<AID> order;

    @Override
    protected void setup() {
        System.out.println("Visitor " + getAID().getName() + " set");

    }

    private class AddDishToOrder extends Behaviour {

        AID toAdd;

        AddDishToOrder(AID dish) {
            toAdd = dish;
        }

        @Override
        public void action() {
            order.add(toAdd);
        }

        @Override
        public boolean done() {
            return true;
        }
    }

    private class DeleteDishFromOrder extends Behaviour {

        AID toDelete;

        DeleteDishFromOrder(AID dish) {
            toDelete = dish;
        }
        @Override
        public void action() {
            order.remove(toDelete);
        }

        @Override
        public boolean done() {
            return true;
        }
    }

    private class MakeOrder extends Behaviour {

        @Override
        public void action() {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID("Manager", AID.ISLOCALNAME));
            msg.setLanguage("English");
            // TODO: Отосолать заказ
            try {
                msg.setContentObject(order);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            send(msg);
            myAgent.addBehaviour(new ReceiveAboutTime());
        }

        @Override
        public boolean done() {
            return true;
        }
    }

    private class CancelOrder extends Behaviour {

        @Override
        public void action() {
            var msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID("Manager", AID.ISLOCALNAME));
            msg.setLanguage("English");
            msg.setContent("Cancel order");
            send(msg);
        }

        @Override
        public boolean done() {
            return true;
        }
    }

    private class ReceiveAboutTime extends Behaviour {

        Boolean isReady = false;
        String timeLeft;

        @Override
        public void action() {
            var msg = myAgent.receive();
            if (msg != null) {
                try {
                    var response = (Pair<Boolean, String>)msg.getContentObject();
                    isReady = response.getFirst();
                    timeLeft = response.getSecond();
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public boolean done() {
            return isReady;
        }
    }

}