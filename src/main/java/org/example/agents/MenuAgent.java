package org.example.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.UnreadableException;
import org.example.models.Menu;

import java.util.ArrayList;

public class MenuAgent extends Agent {

    ArrayList<Menu> thisMenu;
    @Override
    protected void setup() {
        thisMenu = (ArrayList<Menu>) getArguments()[0];

        addBehaviour(new ActualizeMenu());
        addBehaviour(new SendAboutMenu());
    }

    private class ActualizeMenu extends CyclicBehaviour {
        @Override
        public void action() {
            System.out.println("Menu: trying to receive message from manager");
            var msg = myAgent.receive();
            if (msg != null && !msg.getSender().getLocalName().equals("Storage")) {
                Menu response;
                try {
                    response = (Menu) msg.getContentObject();
                } catch (UnreadableException e) {
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
