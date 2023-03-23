package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class ProductAgent extends Agent {
    int prodTypeId;
    String prodTypeName;
    boolean prodIsFood;
    boolean isReserved;
    AID storage;


    @Override
    protected void setup() {
        Object[] args = getArguments();
        prodTypeId = (int) args[0];
        prodTypeName = (String) args[1];
        prodIsFood = (boolean) args[2];
        storage = (AID) args[3];
        isReserved = true;

        // TODO: addBehaviour with certain amount of reservation
    }


    private static class Reserve extends Behaviour {
        Integer reserveAmount;

        Reserve(Integer reserveAmount) {
            this.reserveAmount = reserveAmount;
        }

        @Override
        public void action() {
            var msg = myAgent.receive();
            if (msg != null) {

            } else {
                block();
            }
        }

        @Override
        public boolean done() {
            return true;
        }
    }
}
