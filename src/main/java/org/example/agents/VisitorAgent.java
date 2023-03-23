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
import java.util.List;


public class VisitorAgent extends Agent {
    String name;
    int orderTotal;
    ArrayList<String> order;

    AID manager;

    @Override
    protected void setup() {
        System.out.println("Visitor " + getAID().getName() + " set");
        // TODO: Сделать логику поведения
        manager = getAID("Manager");
        System.out.println(manager.getName());
        order = new ArrayList<>(List.of(new String[]{"something", "stupid", "added", "to", "order"}));
        addBehaviour(new TickerBehaviour(this, 6000) {
            @Override
            protected void onTick() {
                addBehaviour(new MakeOrder());
            }
        });
    }

    private class AddDishToOrder extends Behaviour {

        String toAdd;

        AddDishToOrder(String dish) {
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

        String toDelete;

        DeleteDishFromOrder(String dish) {
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
            System.out.println("Making order");
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(manager);
            msg.setLanguage("English");
            // TODO: Отосолать заказ
            try {
                msg.setContentObject(order);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                if (msg.getContentObject() != null) {
                    System.out.println("message has been set");
                } else {
                    System.out.println("message has not been set");
                }
            } catch (UnreadableException e) {
                throw new RuntimeException(e);
            }
            myAgent.send(msg);
            System.out.println("sent message");
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

    private static class ReceiveAboutTime extends Behaviour {

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
            } else {
                block();
            }
        }

        @Override
        public boolean done() {
            return isReady;
        }
    }

}