package org.example.agents;

import jade.core.*;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.wrapper.StaleProxyException;
import org.example.models.Visitor;

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
//        addBehaviour(new OneShotBehaviour() {
//            @Override
//            public void action() {
//                myAgent.addBehaviour(new DeleteOrder());
//            }
//        });
//        addBehaviour(new OneShotBehaviour() {
//            @Override
//            public void action() {
//                myAgent.addBehaviour(new SendOrderToMenuAgent());
//            }
//        });

        System.out.println("Manager " + getName() + " is set");
        System.out.println(getAID());
    }

    // По тз
    private class CreateOrder extends CyclicBehaviour {
        @Override
        public void action() {
            System.out.println("Manager: trying to receive message");
            var message = myAgent.receive();
            if (message != null) {
                try {
                    var response = (Visitor) message.getContentObject();
                    System.out.println("Manager: Message received from " + response.vis_name);
                    for (var ord : response.vis_ord_dishes) {
                        // TODO: отослать агнету Меню о актуализации меню
                        System.out.print(ord.menu_dish_id + ", ");
                    }
                    System.out.println();
//                    try {
//                        // TODO: Создать ордер агента с нужными параметрами(передать их) Пока передаю ArrayList<AID>
//                        countOfOrders++;
//                        mainContainer.createNewAgent("order " + countOfOrders, OrderAgent.class.getName(), new Object[]{response, countOfOrders, mainContainer}).start();
//                    } catch (StaleProxyException e) {
//                        throw new RuntimeException(e);
//                    }
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
        // TODO: как мы узнаем какие именно продукты резервировать? Пока что просто кинул список AID.
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
            return false;
        }
    }

}
