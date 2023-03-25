package org.example.agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;

public class CookAgent extends Agent {
    Integer id;
    String name;
    Boolean isActive;

    @Override
    protected void setup() {
        var args = getArguments();
        this.id = (Integer) args[0];
        this.name = (String) args[1];
        this.isActive = (Boolean) args[2];

        addBehaviour(new ReserveCook());
    }

    private class ReserveCook extends CyclicBehaviour {

        @Override
        public void action() {
            var msg = myAgent.receive();
            if (msg != null) {
                isActive = msg.getContent().equals("Reserve");
            } else {
                block();
            }
        }
    }
}
