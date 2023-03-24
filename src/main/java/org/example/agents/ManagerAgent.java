package org.example.agents;

import jade.core.*;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;

public class ManagerAgent extends Agent {

    private jade.wrapper.AgentContainer mainContainer;
//    private ArrayList<String> response;
    Integer countOfOrders = 0;

    // TODO: Как я понял, у нас один manager, он создает много заказов и контейнер со всеми агентами мы хроним в managere.
    // Ему через параметры контейнер передается.
    @Override
    protected void setup() {
        mainContainer = (jade.wrapper.AgentContainer)getArguments()[0];
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                myAgent.addBehaviour(new CreateOrder());
            }
        });
        // TODO: как понять когда удалять?
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
                    System.out.println("Manager: Message received from " + message.getSender().getName());
                    var response = (ArrayList<Integer>) message.getContentObject();
                    for (var ord : response) {
                        System.out.println(ord);
                    }
                    try {
                        // TODO: Создать ордер агента с нужными параметрами(передать их) Пока передаю ArrayList<AID>
                        countOfOrders++;
                        var s = new StringBuilder("order ").append(countOfOrders);
                        mainContainer.createNewAgent(s.toString(), OrderAgent.class.getName(), new Object[]{response, countOfOrders, mainContainer}).start();
                    } catch (StaleProxyException e) {
                        throw new RuntimeException(e);
                    }
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
