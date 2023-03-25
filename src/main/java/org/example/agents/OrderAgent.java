package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.StaleProxyException;
import org.example.models.Visitor.Visitor;

import java.io.IOException;

public class OrderAgent extends Agent {
    // Содержит id блюд

    private jade.wrapper.AgentContainer mainContainer;
    private Visitor visitor;

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
