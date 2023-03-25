package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class CookAgent extends Agent {
    double timeToWait;

    @Override
    protected void setup() {
        addBehaviour(new ReserveCook());
    }

    private class ReserveCook extends CyclicBehaviour {

        @Override
        public void action() {
            var msg = myAgent.receive();
            if (msg != null) {
                try {
                    timeToWait = (double)msg.getContentObject();
                    myAgent.wait((long)timeToWait * 60);
                    var message = new ACLMessage(ACLMessage.INFORM);
                    message.addReceiver(new AID(msg.getSender().getLocalName(), AID.ISLOCALNAME));
                    message.setContent("done");
                    send(message);
                } catch (UnreadableException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                block();
            }
        }
    }
}
