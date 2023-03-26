package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.example.Pair;
import org.example.models.Visitor.VisOrdDishes;
import org.example.models.Visitor.Visitor;

import java.io.IOException;
import java.util.Random;


public class VisitorAgent extends Agent {
    Visitor thisVisitor;

    AID manager;

    @Override
    protected void setup() {
        System.out.println("Visitor " + getAID().getName() + " set");
        manager = getAID("Manager");
        // TODO: заполнить нормально заказ посетителя из входных данных
        thisVisitor = (Visitor) getArguments()[0];
        var rnd = new Random();
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                addBehaviour(new MakeOrder());
            }
        });
    }

    private class MakeOrder extends Behaviour {

        @Override
        public void action() {
            System.out.println(myAgent.getLocalName() + ": Making order");
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(manager);
            msg.setLanguage("English");
            try {
                msg.setContentObject(thisVisitor);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                if (msg.getContentObject() != null) {
                    System.out.println(myAgent.getLocalName() + ": message has been set");
                } else {
                    System.out.println(myAgent.getLocalName() + ": message has not been set");
                }
            } catch (UnreadableException e) {
                throw new RuntimeException(e);
            }
            myAgent.send(msg);
            System.out.println(myAgent.getLocalName() + ": sent message");
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
            if (msg != null && !msg.getSender().getLocalName().equals("ams")) {
                try {
                    var response = (Double) msg.getContentObject();
                    System.out.println(myAgent.getLocalName() + ": timeLeft = " + response);
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