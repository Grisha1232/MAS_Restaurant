package org.example.agents;

import jade.core.*;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.wrapper.StaleProxyException;
import org.example.models.Visitor.Visitor;

import java.io.IOException;

public class ManagerAgent extends Agent {

    private jade.wrapper.AgentContainer mainContainer;
    Integer countOfOrders = 0;

    @Override
    protected void setup() {
        mainContainer = (jade.wrapper.AgentContainer)getArguments()[0];
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                myAgent.addBehaviour(new ReceiveMessageFromVisitor());
            }
        });

        System.out.println("Manager " + getName() + " is set");
    }

    // По тз
    private class ReceiveMessageFromVisitor extends CyclicBehaviour {
        @Override
        public void action() {
            System.out.println("Manager: trying to receive message");
            var message = myAgent.receive();
            if (message != null && !message.getSender().getLocalName().equals("Menu") && !message.getSender().getLocalName().equals("ams")) {
                try {
                    var response = (Visitor) message.getContentObject();
                    System.out.println("Manager: Message received from " + response.vis_name);
                    addBehaviour(new SendOrderToMenuAgent(response));
                    for (var ord : response.vis_ord_dishes) {
                        System.out.print(ord.ord_dish_id + ", ");
                    }
                    System.out.println();
                } catch (UnreadableException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                block();
            }
        }
    }
    // по схеме
    private class SendOrderToMenuAgent extends Behaviour {
        Visitor vis;
        SendOrderToMenuAgent(Visitor vis) {
            this.vis = vis;
        }
        @Override
        public void action() {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID("Menu", AID.ISLOCALNAME));
            msg.setLanguage("English");
            try {
                msg.setContentObject(vis);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            send(msg);
            addBehaviour(new ReceiveAnswerFromMenuAgent());
        }

        @Override
        public boolean done() {
            return true;
        }
    }

    private class ReceiveAnswerFromMenuAgent extends Behaviour {

        Visitor vis = null;

        @Override
        public void action() {
            var message = myAgent.receive();
            if (message != null && message.getSender().getLocalName().equals("Menu")) {
                try {
                    var fixedVisitor = (Visitor)message.getContentObject();
                    String nick = "order for " + fixedVisitor.vis_name;
                    mainContainer.createNewAgent(nick, OrderAgent.class.getName(), new Object[]{fixedVisitor}).start();

                } catch (UnreadableException | StaleProxyException e) {
                    throw new RuntimeException(e);
                }
            } else {
                block();
            }
        }

        @Override
        public boolean done() {
            return vis != null;
        }
    }

}
