package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.example.Pair;
import org.example.models.Visitor.VisOrdDishes;
import org.example.models.Visitor.Visitor;

import java.io.IOException;
import java.util.ArrayList;

public class MenuAgent extends Agent {

    @Override
    protected void setup() {
        addBehaviour(new ProcessTheReceivedMessages());
    }


    private class ProcessTheReceivedMessages extends CyclicBehaviour {
        @Override
        public void action() {
            var msg = receive();
            if (msg != null) {
                if (msg.getSender().getLocalName().equals("Storage")) {
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
                } else if (msg.getSender().getLocalName().equals("Manager")) {
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
                    try {
                        var response = (VisOrdDishes) msg.getContentObject();
                        // TODO: как получить из этого операции?
                    } catch (UnreadableException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                block();
            }
        }
    }
}
