package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.example.Parsing.ParsingDishCard;
import org.example.Parsing.ParsingMenu;
import org.example.models.DishCard.OperProduct;
import org.example.models.Menu;
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
                // TODO: запрос Складу об актуальности меню
            } else {
                block();
            }
        }
    }

    private class SendAboutMenu extends CyclicBehaviour {
        @Override
        public void action() {
            System.out.println("Menu: trying to receive message from Storage");
            var msg = myAgent.receive();
            if (msg != null && msg.getSender().getLocalName().equals("Storage")) {
                // TODO: Получив ответ от склада отослать обратно менеджеру
            } else {
                block();
            }
        }
    }
}
