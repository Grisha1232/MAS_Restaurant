package org.example.agents;

import jade.core.*;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.example.models.Visitor.Visitor;

public class ManagerAgent extends Agent {

    private jade.wrapper.AgentContainer mainContainer;
    Integer countOfOrders = 0;

    @Override
    protected void setup() {
        mainContainer = (jade.wrapper.AgentContainer)getArguments()[0];
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                myAgent.addBehaviour(new CreateOrder());
            }
        });

        System.out.println("Manager " + getName() + " is set");
        System.out.println(getAID());
    }

    // По тз
    private class CreateOrder extends CyclicBehaviour {
        @Override
        public void action() {
            System.out.println("Manager: trying to receive message");
            var message = myAgent.receive();
            if (message != null && !message.getSender().getLocalName().equals("Menu")) {
                try {
                    var response = (Visitor) message.getContentObject();
                    System.out.println("Manager: Message received from " + response.vis_name);
                    // TODO: отослать агнету Меню о актуализации меню
                    addBehaviour(new SendOrderToMenuAgent());
                    // TODO: Отсылать нужно с информацией от какого посетителя пришел этот заказ чтобы создать позже заказ

                    for (var ord : response.vis_ord_dishes) {
                        System.out.print(ord.menu_dish_id + ", ");
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

        SendOrderToMenuAgent() {
        }
        @Override
        public void action() {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID("Menu", AID.ISLOCALNAME));
            msg.setLanguage("English");
//            try {
//                msg.setContentObject(response);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
            send(msg);
        }

        @Override
        public boolean done() {
            return true;
        }
    }

    private class ReceiveAnswerFromMenuAgent extends CyclicBehaviour {

        @Override
        public void action() {
            var message = myAgent.receive();
            if (message != null && message.getSender().getLocalName().equals("Menu")) {
                // TODO: сделать заказ с полученным меню
            } else {
                block();
            }
        }
    }

}
