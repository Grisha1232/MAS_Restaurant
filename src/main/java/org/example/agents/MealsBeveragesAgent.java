package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

import java.util.Date;
import java.util.Deque;

public class MealsBeveragesAgent extends Agent {

    @Override
    protected void setup() {
        super.setup();
    }

    private static class CookMeal extends Behaviour {

        private int             processId;
        private int             orderedDish;
        private Date            processStarted;
        private Date            processEnded;
        private boolean         isProcessActive;
        private Deque<AID> processOperations;
        @Override
        public void action() {
            if (!processOperations.isEmpty()) {
                var operation = processOperations.pop();
                // do some operation
            }
        }

        @Override
        public boolean done() {
            return false;
        }
    }
}
