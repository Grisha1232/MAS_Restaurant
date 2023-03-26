package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.wrapper.StaleProxyException;
import org.example.models.Visitor.Visitor;

import java.io.IOException;
import java.util.ArrayList;

public class OrderAgent extends Agent {
    private jade.wrapper.AgentContainer mainContainer;
    private Visitor visitor;
    private ArrayList<String> processIncluded;

    @Override
    protected void setup() {
        var args = getArguments();
        visitor = (Visitor) args[0];

        addBehaviour(new CreateMeals());
    }

    private class CreateMeals extends Behaviour {

        @Override
        public void action() {


            // создание процесса готовки.
            for (var i : visitor.vis_ord_dishes) {
                try {
                    mainContainer.createNewAgent(visitor.vis_name + " " + i.menu_dish,
                            ProcessAgent.class.getName(), new Object[]{i}).start();
                    processIncluded.add(visitor.vis_name + " " + i.menu_dish);
                } catch (StaleProxyException e) {
                    throw new RuntimeException(e);
                }
            }

        }

        @Override
        public boolean done() {
            return true;
        }
    }


    private class NotifyAboutTime extends Behaviour {
        Integer step = 0;
        int receivedMessages = 0;
        Double timeLeft = 0.0;

        @Override
        public void action() {
            if (step == 0) {
                for (var nick : processIncluded) {
                    var msg = new ACLMessage(ACLMessage.INFORM);
                    msg.addReceiver(new AID(nick, AID.ISLOCALNAME));
                    msg.setContent("give me a time left");
                    send(msg);
                }
                step = 1;
            } else if (step == 1) {
                // TODO: receive message from operationAgent about time left (step = 1)
                var msg = receive();
                if (msg != null) {
                    receivedMessages++;
                    try {
                       var time = (Double) msg.getContentObject();
                       if (time > timeLeft) {
                           timeLeft = time;
                       }
                    } catch (UnreadableException e) {
                        throw new RuntimeException(e);
                    }
                    if (receivedMessages == processIncluded.size()) {
                        step = 2;
                    }
                } else {
                    block();
                }
            } else {
                var message = new ACLMessage(ACLMessage.INFORM);
                message.addReceiver(new AID(visitor.vis_name, AID.ISLOCALNAME));
                try {
                    message.setContentObject(timeLeft);
                    send(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public boolean done() {
            return step == 3;
        }
    }

    private class ReserveProduct extends Behaviour{

        @Override
        public void action() {
            var msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID("Storage", AID.ISLOCALNAME));
            try {
                msg.setContentObject(visitor);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            send(msg);
            addBehaviour(new CreateMeals());

        }

        @Override
        public boolean done() {
            return false;
        }
    }


}
