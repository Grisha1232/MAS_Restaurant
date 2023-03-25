package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.example.Pair;
import org.example.models.Visitor.Visitor;

import java.io.IOException;
import java.util.ArrayList;

public class MenuAgent extends Agent {

    @Override
    protected void setup() {

        addBehaviour(new ActualizeMenu());
        addBehaviour(new SendAboutMenu());
    }

    private class ActualizeMenu extends CyclicBehaviour {
        @Override
        public void action() {
            System.out.println("Menu: trying to receive message from manager");
            var msg = myAgent.receive();
            if (msg != null && !msg.getSender().getLocalName().equals("Storage")) {
                try {
                    Visitor vis = (Visitor) msg.getContentObject();
                    var message = new ACLMessage(ACLMessage.INFORM);
                    message.addReceiver(new AID("Storage", AID.ISLOCALNAME));
                    message.setContentObject(vis);
                    send(message);
                } catch (UnreadableException | IOException e) {
                    throw new RuntimeException(e);
                }

            } else {
                block();
            }
        }
    }
    //TODO: дописать чтоб из блюда получалась операция.
    private class SendAboutMenu extends CyclicBehaviour {
        @Override
        public void action() {
            System.out.println("Menu: trying to receive message from Storage");
            var msg = myAgent.receive();
            if (msg != null && msg.getSender().getLocalName().equals("Storage")) {
                try {
                    var pair = (Pair<Visitor, ArrayList<Integer>>)msg.getContentObject();
                    var vis = pair.getFirst();
                    var unv = pair.getSecond();
                    // Удаляем блюда, которые не можем приготовить
                    for (var i : unv) {
                        vis.vis_ord_dishes.removeIf((x) -> x.menu_dish == i);
                    }
                    var message = new ACLMessage(ACLMessage.INFORM);
                    message.addReceiver(new AID("Manager", AID.ISLOCALNAME));
                    message.setContentObject(vis);
                    send(message);
                } catch (UnreadableException | IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                block();
            }
        }
    }
}
