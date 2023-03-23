package org.example.agents;

import jade.core.*;
import jade.core.Runtime;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

import javax.sound.sampled.DataLine;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringJoiner;

public class ManagerAgent extends Agent {

    private jade.wrapper.AgentContainer mainContainer;
    private ArrayList<AID> response;
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
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                myAgent.addBehaviour(new DeleteOrder());
            }
        });
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                myAgent.addBehaviour(new SendOrderToMenuAgent());
            }
        });

    }

    // По тз
    private class CreateOrder extends Behaviour {
        @Override
        public void action() {
            var message = myAgent.receive();
            if (message != null) {
                try {
                    response = (ArrayList<AID>) message.getContentObject();
                    try {
                        // TODO: Создать ордер агента с нужными параметрами(передать их) Пока передаю ArrayList<AID>
                        countOfOrders++;
                        var s = new StringBuilder("order ").append(countOfOrders);
                        mainContainer.createNewAgent(s.toString(), OrderAgent.class.getName(), new ArrayList[]{response}).start();
                    } catch (StaleProxyException e) {
                        throw new RuntimeException(e);
                    }
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }
            } else {
                block();
            }
        }

        @Override
        public boolean done() {
            return true;
        }
    }

    // По тз
    private class DeleteOrder extends Behaviour {

        @Override
        public void action() {
            // TODO: нужно опеределить как удалять ордер агента, пока что делаю по никнейму.
            var nickToDelete = myAgent.receive();
            if (nickToDelete != null) {
                try {
                    mainContainer.getAgent(nickToDelete.getContent()).kill();
                } catch (ControllerException e) {
                    throw new RuntimeException("Didn't find nickname of agent");
                }
            } else {
                block();
            }

        }

        @Override
        public boolean done() {
            return true;
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
            try {
                msg.setContentObject(response);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            send(msg);
        }

        @Override
        public boolean done() {
            return false;
        }
    }

}
